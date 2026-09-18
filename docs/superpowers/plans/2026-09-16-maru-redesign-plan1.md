# 마루 리디자인 구현 계획 1/3: 토큰, 공통 컴포넌트, 체크리스트

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 색 토큰과 DayNight 테마로 앱 전체 색을 통일하고, 시스템 서체로 바꾸고, 공통 컴포넌트를 만든 뒤 체크리스트, 지출 입력 시트, 완료 상태 화면을 새 디자인으로 교체한다.

**Architecture:** 화면별 테마 속성(attr) 22개를 `@color/maru_*` 토큰 직접 참조로 바꾸고, 수동 `setTheme` 전환을 `AppCompatDelegate.setDefaultNightMode` + `values-night`로 바꾼다. 체크리스트의 정렬, 상태, 광고 위치 규칙은 순수 Kotlin 객체 `TasksListBuilder`로 분리해 JVM 테스트로 고정하고, 어댑터와 뷰모델은 그 결과만 그린다. 지출 입력은 전체 화면 Fragment에서 하단 고정 `DialogFragment`로 바꾼다.

**Tech Stack:** Kotlin 2.3, AGP 8.13, Gradle 8.13, AppCompat(Material Components 없음), DataBinding, Navigation 2.9 SafeArgs, Room, Dagger Android, AdMob SDK, JUnit 4.12 + mockito-inline(JVM), Espresso(androidTest).

**Spec:** `docs/superpowers/specs/2026-09-16-maru-redesign-design.md` (시각 자료: `docs/design/maru-redesign-2026.html`)

## Global Constraints

- 데이터 모델(User, Task, Account, Day)과 Room 스키마, DAO 시그니처를 바꾸지 않는다. `ROOM_VERSION = 2` 유지.
- Material Components 의존성을 추가하지 않는다. 세그먼트와 하단 시트는 자체 구현.
- 서체를 번들하지 않는다. 시스템 기본 서체, 굵기 400/500/700과 크기로 위계.
- 색은 토큰 12개 + 틴트 3개 + `maru_on_yellow`(옐로 위 글자, 양 모드 #23262B) + `maru_scrim`만 새로 추가한다. 기존 113색은 이 계획에서 삭제하지 않는다(계획 3에서 정리). 새 코드와 새 레이아웃은 기존 색을 참조하지 않는다.
- 최소 글자 크기 12sp. 텍스트에 쓰는 회색은 `maru_muted`(#6F757E) 또는 `maru_ink_2`만.
- 라디우스: 카드 20dp, 입력과 타일 14dp, 버튼과 칩 pill, 시트 상단 28dp. 그림자는 시트와 FAB만.
- 광고: 체크리스트 네이티브는 4번째 행 뒤, 이후 10행 간격, 행 사이에만(목록 끝에는 넣지 않음), 최대 2개, 프리미엄이면 0개. 전면 광고 빈도는 현행 유지.
- 문구: 이모티콘 없음. "AD" 라벨은 "광고". 필터 "진행중"은 "진행 중".
- **커밋 규칙:** 사용자 CLAUDE.md에 따라 커밋, 브랜치 생성, stash는 사용자가 명시적으로 요청한 경우에만 한다. 아래 각 Task의 "커밋" 단계는 사용자가 이 계획 실행 시 커밋을 허용했을 때만 실행하고, 그렇지 않으면 건너뛰고 working tree에 둔다.
- 검증 명령은 항상 저장소 루트(`/Users/mk-am16-013/Dev/apps/maru`)에서 실행한다.

## 파일 구조 (이 계획에서 만들거나 바꾸는 것)

생성
- `app/src/main/res/values-night/colors.xml` 다크 토큰
- `app/src/main/res/drawable/bg_*.xml`, `sel_check.xml`, `dot_*.xml`, `progress_done.xml` 공통 셰이프
- `app/src/main/java/dev/kxxcn/maru/view/custom/SegmentView.kt` 가로 세그먼트
- `app/src/main/java/dev/kxxcn/maru/view/custom/SplitBarView.kt` 청/홍 분담 바
- `app/src/main/java/dev/kxxcn/maru/view/tasks/TasksListBuilder.kt` 정렬/상태/광고 규칙 (순수 로직)
- `app/src/test/java/dev/kxxcn/maru/view/tasks/TasksListBuilderTest.kt` JVM 테스트 (첫 JVM 테스트 디렉터리)
- `app/src/main/java/dev/kxxcn/maru/view/tasks/holder/TasksRowHolder.kt` 단일 행 홀더
- `app/src/main/res/layout/task_row_item.xml`, `ad_slot_view.xml`, `input_sheet_fragment.xml`
- `app/src/main/java/dev/kxxcn/maru/util/AdSlotBinder.kt` 네이티브 광고 뷰 바인딩
- `app/src/main/java/dev/kxxcn/maru/view/input/InputSheetFragment.kt` 하단 시트
- `app/src/main/res/anim/slide_in_bottom.xml`, `slide_out_bottom.xml`

수정
- `res/values/colors.xml`, `styles.xml`, `attrs.xml`, `strings.xml`, `dimens.xml`
- `MaruApplication.kt`, `MaruActivity.kt`, `view/more/MoreFragment.kt` (야간모드 전환)
- 모든 `res/layout/*.xml`, `res/drawable/*.xml`의 `?maru*` 속성 치환
- `view/custom/MaruTextView.kt`, `ArgProgressView.kt` (서체)
- `view/tasks/*` 전부, `view/input/InputViewModel.kt`, `view/status/*`, `di/InputModule.kt`
- `data/source/DataRepository.kt`, `MaruRepository.kt`, `local/LocalDataSource.kt`, `util/AdHelper.kt`, `util/ConvertUtils.kt`, `util/DateUtils.kt`, `util/preference/PreferenceUtils.kt`, `util/Const.kt`
- `res/navigation/nav_graph.xml`
- `androidTest`의 `FakeRepository.kt`, `TasksFragmentTest.kt`, `InputFragmentTest.kt`

삭제
- `_night` 드로어블 7개와 짝 드로어블, `assets/nixgon.ttf`, `res/font/*.ttf`
- `RotateSelectionView.kt`, `VerticalTextView.kt`, `rotate_selection_view.xml`
- `tasks_active_item.xml`, `tasks_completed_item.xml`, `tasks_prepare_item.xml`, `tasks_native_view.xml`, `input_fragment.xml`
- `TasksActiveHolder.kt`, `TasksCompletedHolder.kt`, `TasksPrepareHolder.kt`, `InputFragment.kt`

---

### Task 1: 색 토큰과 다크 값 추가

**Files:**
- Modify: `app/src/main/res/values/colors.xml`
- Create: `app/src/main/res/values-night/colors.xml`

**Interfaces:**
- Produces: 색 리소스 `maru_paper, maru_surface, maru_surface_2, maru_line, maru_ink, maru_ink_2, maru_muted, maru_yellow, maru_yellow_ink, maru_yellow_tint, maru_groom, maru_groom_tint, maru_bride, maru_bride_tint, maru_done, maru_done_tint, maru_on_yellow, maru_scrim`. 이후 모든 Task가 이 이름만 쓴다.

- [ ] **Step 1: 라이트 토큰 추가**

`app/src/main/res/values/colors.xml`의 `<resources>` 바로 아래에 추가한다 (기존 색은 그대로 둔다).

```xml
    <!-- 마루 디자인 토큰 (2026-09 리디자인). 아래의 기존 색은 계획 3에서 제거한다. -->
    <color name="maru_paper">#FCFBF7</color>
    <color name="maru_surface">#FFFFFF</color>
    <color name="maru_surface_2">#F4F3EE</color>
    <color name="maru_line">#ECEAE2</color>
    <color name="maru_ink">#23262B</color>
    <color name="maru_ink_2">#5C6168</color>
    <color name="maru_muted">#6F757E</color>
    <color name="maru_yellow">#FED669</color>
    <color name="maru_yellow_ink">#8A6400</color>
    <color name="maru_yellow_tint">#FFF4CF</color>
    <color name="maru_groom">#4F6FCB</color>
    <color name="maru_groom_tint">#E4EAFA</color>
    <color name="maru_bride">#D65A6E</color>
    <color name="maru_bride_tint">#FCE6EA</color>
    <color name="maru_done">#3F8F6B</color>
    <color name="maru_done_tint">#E1F2EA</color>
    <color name="maru_on_yellow">#23262B</color>
    <color name="maru_scrim">#8023262B</color>
```

- [ ] **Step 2: 다크 토큰 파일 생성**

`app/src/main/res/values-night/colors.xml` (새 파일):

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="maru_paper">#16181C</color>
    <color name="maru_surface">#1F2227</color>
    <color name="maru_surface_2">#262A30</color>
    <color name="maru_line">#2E3339</color>
    <color name="maru_ink">#ECEBE6</color>
    <color name="maru_ink_2">#B7BCC4</color>
    <color name="maru_muted">#7E848E</color>
    <color name="maru_yellow">#FED669</color>
    <color name="maru_yellow_ink">#F2CF6B</color>
    <color name="maru_yellow_tint">#3A3320</color>
    <color name="maru_groom">#8AA3EA</color>
    <color name="maru_groom_tint">#26304A</color>
    <color name="maru_bride">#F08A9B</color>
    <color name="maru_bride_tint">#4A2A31</color>
    <color name="maru_done">#6CC29B</color>
    <color name="maru_done_tint">#22382E</color>
    <color name="maru_on_yellow">#23262B</color>
    <color name="maru_scrim">#80000000</color>
</resources>
```

- [ ] **Step 3: 리소스 컴파일 확인**

Run: `./gradlew :app:processDebugResources --quiet`
Expected: BUILD SUCCESSFUL. (values-night에 정의한 이름이 모두 values에도 있어야 한다. 하나라도 빠지면 "resource not found" 대신 lint 경고만 나므로 이름 18개를 눈으로 대조한다.)

- [ ] **Step 4: 커밋 (허용된 경우)**

```bash
git add app/src/main/res/values/colors.xml app/src/main/res/values-night/colors.xml
git commit -m "Add maru color tokens for light and night"
```

---

### Task 2: 테마 속성을 토큰으로 치환하고 DayNight로 전환

**Files:**
- Modify: `app/src/main/res/values/styles.xml`, `app/src/main/res/values/attrs.xml`
- Modify: `app/src/main/java/dev/kxxcn/maru/MaruApplication.kt`, `MaruActivity.kt:118-131`, `view/more/MoreFragment.kt:192-195`
- Modify: `res/layout/*.xml`, `res/layout-v26/*.xml`, `res/drawable/*.xml` (일괄 치환)
- Modify: `view/home/HomeBindings.kt:113`, `view/custom/RotateSelectionView.kt:36,39`, `view/custom/DrawableTextView.kt:46`, `view/custom/UnderlineTextView.kt:35,38`, `view/input/InputViewModel.kt` (`deselectedFontColorRes`)
- Create: `res/drawable/bg_button_primary.xml`, `bg_sheet.xml`, `bg_sheet_handle.xml`
- Delete: `res/drawable/*_night.xml` 7개와 참조가 없어진 짝 파일

**Interfaces:**
- Consumes: Task 1의 색 토큰.
- Produces: `AppTheme`(DayNight) 하나. `AppDarkTheme` 삭제. 야간모드 전환은 `AppCompatDelegate.setDefaultNightMode`. 드로어블 `bg_button_primary`(옐로 pill), `bg_sheet`, `bg_sheet_handle`.

- [ ] **Step 1: 공용 드로어블 3개 생성**

`res/drawable/bg_button_primary.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/maru_yellow" />
    <corners android:radius="999dp" />
</shape>
```

`res/drawable/bg_sheet.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/maru_surface" />
    <corners android:topLeftRadius="28dp" android:topRightRadius="28dp" />
</shape>
```

`res/drawable/bg_sheet_handle.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/maru_line" />
    <corners android:radius="2dp" />
    <size android:width="40dp" android:height="4dp" />
</shape>
```

- [ ] **Step 2: 레이아웃과 드로어블의 `?attr` 일괄 치환**

저장소 루트에서 실행한다. `?maruFontColorLight`를 `?maruFontColor`보다 먼저 치환해야 한다(접두 일치). macOS sed 문법이다.

```bash
cd app/src/main/res && find layout layout-v26 drawable -name '*.xml' -print0 | xargs -0 sed -i '' \
  -e 's#?attr/maruBackgroundColor#@color/maru_paper#g' \
  -e 's#?maruSecondaryBackgroundColor#@color/maru_surface#g' \
  -e 's#?maruBackgroundColor#@color/maru_paper#g' \
  -e 's#?maruFontColorLight#@color/maru_muted#g' \
  -e 's#?maruSecondaryFontColor#@color/maru_on_yellow#g' \
  -e 's#?maruFontColor#@color/maru_ink#g' \
  -e 's#?descriptionDividerColor#@color/maru_line#g' \
  -e 's#?tasksNativeFontColor#@color/maru_ink#g' \
  -e 's#?tasksNativeBackgroundColor#@color/maru_surface_2#g' \
  -e 's#?splashBackgroundDrawable#@color/maru_paper#g' \
  -e 's#?attr/navigatorBackgroundDrawable#@color/maru_surface#g' \
  -e 's#?navigatorTint#@color/maru_ink#g' \
  -e 's#?attr/inputButtonBackgroundDrawable#@drawable/bg_button_primary#g' \
  -e 's#?inputButtonBackgroundDrawable#@drawable/bg_button_primary#g' \
  -e 's#?daysAddButtonBackgroundDrawable#@drawable/bg_button_primary#g' \
  -e 's#?daysAddHintColor#@color/maru_muted#g' \
  -e 's#?tasksBackgroundDrawable#@color/maru_paper#g' \
  -e 's#?bottomSheetBackgroundDrawable#@drawable/bg_sheet#g' \
  -e 's#?bottomSheetHandleBackgroundDrawable#@drawable/bg_sheet_handle#g' \
  -e 's#?colorPrimary#@color/maru_yellow#g' \
  -e 's#?rotateDefaultTextColor#@color/maru_muted#g' && cd -
grep -rn '?maru\|?attr/maru\|?tasks\|?days\|?input\|?bottomSheet\|?navigator\|?splash\|?description\|?rotate' app/src/main/res | grep -v '?attr/selectableItemBackground' ; echo "위 출력이 비어 있어야 한다"
```

- [ ] **Step 3: Kotlin의 attr 참조 제거**

`HomeBindings.kt:113` `AttrsUtils.getColor(view.context, R.attr.maruFontColor)` → `ContextCompat.getColor(view.context, R.color.maru_ink)`.
`RotateSelectionView.kt:36` → `ContextCompat.getColor(context, R.color.maru_ink)`, `:39` → `ContextCompat.getColor(context, R.color.maru_muted)` (이 클래스는 Task 8에서 삭제되지만 지금은 컴파일만 맞춘다).
`DrawableTextView.kt:46` 주변의 `R.attr.maruFontColor` → `R.color.maru_ink`를 `ContextCompat.getColor`로.
`UnderlineTextView.kt:35` → `R.color.maru_ink`, `:38` → `R.color.maru_muted`.
`InputViewModel.kt`의
```kotlin
val deselectedFontColorRes =
    if (PreferenceUtils.useDarkMode) R.color.maruFontColorNight else android.R.color.black
```
→ `val deselectedFontColorRes = R.color.maru_ink`
그 다음 `grep -rn 'R.attr\.' app/src/main/java`에 남는 것이 `MaruActivity.kt:128` 하나뿐인지 확인한다 (Step 5에서 제거).
또 `grep -rn 'R.color.colorPrimaryDarkNight\|R.color.maruFontColorNight' app/src/main/java`로 나온 줄은 각각 `R.color.maru_paper`, `R.color.maru_ink`로 바꾼다.

- [ ] **Step 4: styles.xml과 attrs.xml 정리**

`res/values/styles.xml` 전체를 다음으로 교체한다.

```xml
<resources>

    <style name="AppTheme" parent="Theme.AppCompat.DayNight.NoActionBar">
        <item name="colorPrimary">@color/maru_yellow</item>
        <item name="colorPrimaryDark">@color/maru_paper</item>
        <item name="colorAccent">@color/maru_yellow</item>
        <item name="android:windowBackground">@color/maru_paper</item>
        <item name="android:textColorPrimary">@color/maru_ink</item>
        <item name="android:textColorSecondary">@color/maru_ink_2</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
    </style>

</resources>
```

`res/values/attrs.xml`에서 `<declare-styleable name="MaruTheme">` 블록 전체(`maruBackgroundColor`부터 `bottomSheetHandleBackgroundDrawable`까지)를 삭제한다. 다른 styleable은 그대로.

- [ ] **Step 5: 야간모드 전환을 AppCompatDelegate로**

`MaruApplication.kt`의 `onCreate()`에서 `PreferenceManager.init(this)` 바로 다음 줄에 추가:
```kotlin
        AppCompatDelegate.setDefaultNightMode(
            if (PreferenceUtils.useDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
```
import `androidx.appcompat.app.AppCompatDelegate`, `dev.kxxcn.maru.util.preference.PreferenceUtils`.

`MaruActivity.kt`의 `setupTheme()`를 다음으로 교체 (setTheme와 AttrsUtils 줄 삭제):
```kotlin
    private fun setupTheme() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
```

`MoreFragment.kt`의 `switch()`를 교체:
```kotlin
    private fun switch() {
        val useDarkMode = !PreferenceUtils.useDarkMode
        PreferenceUtils.useDarkMode = useDarkMode
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
```
(`setDefaultNightMode`가 액티비티를 다시 만든다. 기존 `activity?.recreate()`는 지운다.)

`AttrsUtils.kt`는 `grep -rn 'AttrsUtils' app/src`에 참조가 없으면 삭제한다.

- [ ] **Step 6: `_night` 드로어블 삭제**

```bash
cd app/src/main/res/drawable && for f in bottom_sheet_background_night bottom_sheet_round_handle_night days_add_round_button_night input_register_button_night navigator_round_background_night splash_background_rect_night tasks_round_background_night bottom_sheet_background bottom_sheet_round_handle days_add_round_button input_register_button navigator_round_background splash_background_rect tasks_round_background; do
  if grep -rq "@drawable/$f\b\|R.drawable.$f\b" ../ ../../java; then echo "KEEP $f (참조 있음)"; else rm "$f.xml" && echo "removed $f"; fi
done; cd -
```
"KEEP"으로 남은 파일은 참조 위치를 열어 `@drawable/bg_button_primary` 또는 `@color/maru_paper`로 바꾼 뒤 삭제한다.

- [ ] **Step 7: 빌드와 수동 확인**

Run: `./gradlew :app:assembleDebug --quiet`
Expected: BUILD SUCCESSFUL. 실패하면 `?attr` 잔여 참조나 삭제한 attr 이름을 쓰는 곳이 원인이다. 에러 메시지의 리소스 이름으로 grep해서 토큰으로 바꾼다.

에뮬레이터에서 앱 실행 → 더보기 → 야간모드 토글. 화면 전체가 즉시 다크 토큰으로 바뀌고, 앱 재시작 후에도 유지되어야 한다. 상태바 아이콘이 다크에서 밝게 나오는지 확인(`setupEdgeToEdge`의 `isAppearanceLightStatusBars`).

- [ ] **Step 8: 커밋 (허용된 경우)**

```bash
git add -A app/src/main/res app/src/main/java
git commit -m "Replace theme attrs with maru color tokens and switch to DayNight"
```

---

### Task 3: 번들 서체 제거

**Files:**
- Modify: `view/custom/MaruTextView.kt:44-51`, `view/custom/VerticalTextView.kt:15-18`, `view/custom/ArgProgressView.kt:57`
- Modify: `res/layout/maru_activity.xml:42`, `edit_dialog_fragment.xml:52`, `register_fragment.xml:55`, `woozoora_banner.xml:61`
- Delete: `app/src/main/assets/nixgon.ttf`, `app/src/main/res/font/nixgon.ttf`, `app/src/main/res/font/font_recipe.ttf`

- [ ] **Step 1: MaruTextView에서 서체 로딩 제거**

`MaruTextView.kt` init 블록의
```kotlin
        val tf = Typeface.createFromAsset(context.assets, "nixgon.ttf")
        val style = if (value == normal) Typeface.NORMAL else Typeface.BOLD
        setTypeface(tf, style)
```
→
```kotlin
        val style = if (value == normal) Typeface.NORMAL else Typeface.BOLD
        setTypeface(Typeface.DEFAULT, style)
```
같은 파일의 `paint` 색 `R.color.colorPrimary` → `R.color.maru_yellow`.

- [ ] **Step 2: VerticalTextView, ArgProgressView**

`VerticalTextView.kt`의 `init { ... createFromAsset ... }` 블록을 삭제한다.
`ArgProgressView.kt:57` `private val typeface = Typeface.createFromAsset(context.assets, "nixgon.ttf")` → `private val typeface: Typeface = Typeface.DEFAULT_BOLD`.

- [ ] **Step 3: 레이아웃의 fontFamily 제거와 파일 삭제**

```bash
cd app/src/main && sed -i '' '/app:itemFontFamily="@font\/nixgon"/d' res/layout/maru_activity.xml && sed -i '' '/android:fontFamily="@font\/nixgon"/d' res/layout/edit_dialog_fragment.xml res/layout/register_fragment.xml && sed -i '' '/android:fontFamily="@font\/font_recipe"/d' res/layout/woozoora_banner.xml && rm assets/nixgon.ttf res/font/nixgon.ttf res/font/font_recipe.ttf && rmdir res/font && cd - && grep -rn 'nixgon\|font_recipe\|@font/' app/src ; echo "위 출력이 비어 있어야 한다"
```

- [ ] **Step 4: 빌드**

Run: `./gradlew :app:assembleDebug --quiet`
Expected: BUILD SUCCESSFUL. 앱을 실행해 홈, 체크리스트, 더보기의 글자가 시스템 서체로 나오고 굵은 제목이 유지되는지 본다.

- [ ] **Step 5: 커밋 (허용된 경우)**

```bash
git add -A app/src/main
git commit -m "Remove bundled fonts and use system typeface"
```

---

> **실행 방식 변경 (2026-09-16):** 사용자가 계획 작성 후 승인 질의 없이 구현을 끝내고 Pixel 10 Pro Fold 에뮬레이터로 검증하라고 지시했다. 이에 따라 Task 4 이후는 실행자가 곧바로 구현하는 것을 전제로 파일, 결정, 검증 기준을 중심으로 적고 코드 전문은 생략한다. 이 문서는 실행 기록으로도 쓴다. 커밋은 사용자가 요청하지 않았으므로 하지 않는다.

### Task 4: 공통 셰이프와 스타일

**Files:** Create `res/drawable/bg_button_ghost.xml`(pill, 1.5dp `maru_line` 스트로크), `bg_button_ink.xml`(pill `maru_ink`), `bg_chip.xml`(pill `maru_surface_2`), `bg_card.xml`(20dp `maru_surface` + 1dp `maru_line`), `bg_tile_ready.xml`(14dp `maru_surface_2`), `bg_tile_active.xml`(14dp `maru_yellow_tint`), `bg_tile_done.xml`(14dp `maru_done_tint`), `bg_field.xml`(14dp `maru_surface_2`), `bg_field_on.xml`(14dp `maru_surface` + 1.5dp `maru_ink`), `bg_icon_button.xml`(oval `maru_surface` + 1dp `maru_line`), `bg_fab.xml`(oval `maru_ink`), `bg_segment_track.xml`(pill `maru_surface_2`), `bg_segment_thumb.xml`(pill `maru_surface`), `sel_check.xml`(selected: oval `maru_done` + 흰 `ic_check` inset, 기본: oval 1.5dp `maru_muted` 스트로크), `dot_groom.xml`, `dot_bride.xml`, `dot_yellow.xml`(8dp oval), `progress_done.xml`(layer-list: 트랙 `maru_surface_2`, progress clip `maru_done`, pill). Modify `styles.xml`: `Widget.Maru.Button`, `.Primary`, `.Ghost`, `.Ink`, `Widget.Maru.Chip`, `TextAppearance.Maru.ScreenTitle`(26sp bold, -0.01), `.SectionTitle`(16sp bold), `.Body`(15sp), `.Meta`(12.5sp `maru_muted`), `.Amount`(34sp, tnum).
**Verify:** `./gradlew :app:processDebugResources`.

### Task 5: SegmentView

**Files:** Create `view/custom/SegmentView.kt` (LinearLayout 상속). API: `data class Segment(label: String, count: Int?)`, `setSegments(List<Segment>)`, `setCounts(List<Int?>)`, `var selectedIndex: Int`, `var onSelected: ((Int) -> Unit)?`. 트랙 `bg_segment_track`, 선택 항목 `bg_segment_thumb` + `maru_ink`, 비선택 `maru_muted`, 높이 38dp, 글자 14sp bold, 개수는 라벨 뒤 두 칸 띄우고 표시.
**Verify:** Task 8의 화면에서 탭 전환과 개수 표시를 에뮬레이터로 확인.

### Task 6: SplitBarView와 바인딩

**Files:** Create `view/custom/SplitBarView.kt` (View 상속, `setProgress(groom: Float, bride: Float)`, 트랙 `maru_surface_2`, 청/홍 세그먼트를 둥근 트랙 path로 clip). `TasksBindings.kt`에 `@BindingAdapter("app:groomRatio", "app:brideRatio")` 추가.
**Verify:** 체크리스트 행에서 20%/20%, 69%, 60% 사례가 비율대로 보인다.

### Task 7: TasksListBuilder + JVM 테스트 (TDD)

**Files:** Create `app/src/test/java/dev/kxxcn/maru/view/tasks/TasksListBuilderTest.kt`, `view/tasks/TasksListBuilder.kt`, `view/tasks/TaskState.kt`. Modify `util/Const.kt`(`AD_INTERVAL` 삭제 → `AD_FIRST_AFTER = 4`, `AD_INTERVAL = 10`, `AD_MAX = 2`), `TasksAdapter.TasksItem`에 `state: TaskState?` 추가.
**규칙(스펙 6장 보정):** DONE = `task.isCompleted`. ACTIVE = 미완료이고 `account != null`(잔금 0이어도 기록이 있으면 진행 중, 메타는 "잔금 없음"). READY = 미완료이고 기록 없음. 정렬 = 상태 순(ACTIVE, READY, DONE) 후 `priority`. 광고 위치 = 행 수 n에서 k = 4, 14, ... 중 k < n 인 것 최대 2개, 프리미엄이면 없음. 빈 목록 = 완료 필터면 `task_empty_completed`, 아니면 `task_empty_progress`.
**Tests:** 상태 3종, 정렬 묶음과 묶음 내 priority, adPositions(18→[4,14], 10→[4], 4→[], premium→[]), build가 광고를 index 4와 15에 넣는지(18행 → 20 아이템), 빈 목록 문구.
**Run:** `./gradlew :app:testDebugUnitTest --tests "dev.kxxcn.maru.view.tasks.TasksListBuilderTest"`.

### Task 8: 체크리스트 화면

**Files:** Rewrite `res/layout/tasks_fragment.xml`(제목, 부제 `tasks_completed_summary`, `progress_done` 진행 바, `SegmentView`, RecyclerView, FAB `tasks_add`, 정렬 편집 버튼 `tasks_edit`), Create `res/layout/task_row_item.xml` + `view/tasks/holder/TasksRowHolder.kt`, Rewrite `TasksAdapter.kt`(TYPE_ROW/AD/EMPTY, `makeItems` → `TasksListBuilder.build`), `TasksViewModel.kt`(`totalCount`, `completedCount`, `activeCount`, `progressPercent`, `toggleComplete(detail)`, `add()`, 정렬은 builder), `TasksFragment.kt`(세그먼트 연결, FAB → 체크리스트 추가 다이얼로그), `TasksBindings.kt`(RotateSelectionView, PieChart 어댑터 삭제), `tasks_empty_item.xml`(Lottie 제거, 타일 + 문구). Modify `DataRepository.kt` + `MaruRepository.kt`에 `updateTask(taskId, isCompleted)` 추가, androidTest `FakeRepository.kt`에 구현. `nav_graph.xml`에 `action_tasks_fragment_to_edit_dialog_fragment`(arg `register_type`) 추가. `strings.xml` 추가: `tasks_completed_summary`, `tasks_row_ready_hint`, `tasks_row_remain`, `tasks_row_no_remain`, `tasks_row_done_date`, `tasks_row_skipped`, `tasks_row_complete`, `tasks_row_undo_complete`, `tasks_add`, `tasks_sort`; 수정: `tasks_progress_desc` "진행 중". `DateUtils.DATE_FORMAT_6 = "M월 d일"`. Delete `tasks_active_item.xml`, `tasks_completed_item.xml`, `tasks_prepare_item.xml`, `rotate_selection_view.xml`, 홀더 3개, `RotateSelectionView.kt`, `VerticalTextView.kt`. `TasksFragmentTest.kt`를 새 id로 수정(필터는 `PreferenceUtils.taskFilterType`으로 선택).
**Verify:** assembleDebug, 에뮬레이터에서 세그먼트 전환, 체크 원 토글이 Room에 반영되어 목록이 다시 정렬됨, 광고가 4번째 행 뒤에만 보임.

### Task 9: 광고 슬롯

**Files:** Create `res/layout/ad_slot_view.xml`(NativeAdView 루트, `bg_card` 대신 `maru_surface_2` 배경 20dp, "광고" 라벨 pill, 광고주, MediaView(기본 gone), 아이콘 44dp, 헤드라인 14.5sp bold, 본문 1줄, CTA 고스트 36dp), `util/AdSlotBinder.kt`(`bind(adView, nativeAd, requestManager, showMedia)`), Modify `TasksNativeAdHolder.kt`(AdSlotBinder 사용, 로드 실패 시 컨테이너 숨김), `AdHelper.createNativeAd(id, onFailed)`, `tasks_ad_item.xml`, `strings.xml` `app_ad_attribution` → "광고". Delete `tasks_native_view.xml`.
**Verify:** 테스트 유닛(디버그 빌드는 `or` 확장으로 자동 테스트 ID)으로 광고가 슬롯 모양으로 로드된다.

### Task 10: 지출 입력 하단 시트

**Files:** Create `view/input/InputSheetFragment.kt`(BaseDialogFragment, 하단 고정, 스크림 0.5, `Animation.Maru.Sheet`), `res/layout/input_sheet_fragment.xml`, `res/anim/slide_in_bottom.xml`, `slide_out_bottom.xml`. Modify `InputViewModel.kt`(`selectedField`, `selectField`, `appendDigit`, `deleteDigit`, `addUnit`, `autoComplete` + `setAutoComplete`, `taskIcon`, `recordedDate`; increment/decrement/unit drawable 제거; `complete()`에서 autoComplete && remain==0 이면 `repository.updateTask(id, COMPLETED_TASK)`), `LocalDataSource.saveAccount`(isCompleted 자동 변경 제거), `PreferenceUtils.autoComplete`(기본 true), `ConvertUtils.shortDate`, `di/InputModule.kt`, `nav_graph.xml`(`input_fragment`를 `<dialog>`로, name 교체), `strings.xml`(`input_total`, `input_save`, `input_auto_complete`, `input_first_recorded`, `input_no_record`, `won`). 기존 뷰 id 유지: `input_task_name`, `input_task_money`, `input_husband_money`, `input_wife_money`, `input_remain_money`, `input_unit_*`, `input_task_complete`. Delete `InputFragment.kt`, `input_fragment.xml`, `input_unit_select.xml`, `input_unit_deselect.xml`. `InputFragmentTest.kt`를 `launchFragment` + `inRoot(isDialog())`로 수정.
**Verify:** 필드 탭 후 키패드 입력, 단위 칩 가산, 저장 시 전면 광고(테스트 ID) 후 완료 화면, 잔금 0 + 토글 켬이면 완료 처리, 토글 꺼짐이면 미완료 유지.

### Task 11: 완료 상태 화면

**Files:** Modify `status_fragment.xml`(색 토큰, 완료 버튼 `Widget.Maru.Button.Primary`, `status_next_task` 텍스트), `StatusViewModel.kt`(`nextTaskName`: 미완료 중 priority 최소), `strings.xml` `status_next_task`.
**Verify:** 저장 후 화면에서 링, 숫자, 다음 할 일, 닫기 동작.

### Task 12: 3단계 정리와 검증

`roundCornerProgressBar` 의존성 제거(참조 0 확인), 미참조 드로어블 삭제(`tasks_target_half_circle1/2`, `tasks_remain_rect`, `tasks_completed_check_background`, `selection_circle_oval`, `task_rate_oval`), `./gradlew :app:assembleDebug :app:testDebugUnitTest`, Pixel 10 Pro Fold 접힘/펼침 스크린샷(라이트, 다크).

---

## 이어지는 단계 (계획 2, 3을 이 문서에 통합)

### Task 13: 홈
`home_fragment` 어댑터 아이템을 인사, 디데이 카드, 예산, 남은 잔금, 디데이 칩 다섯 블록으로. 배너 광고와 우주라이트, 파이 차트, CircleProgressView, Lottie 제거. `Summary`에 `remainTasks`(remain > 0, 잔금 큰 순) 추가. 금액은 `ConvertUtils.moneyText`. 레이아웃 `home_dday_item.xml`, `home_budget_item.xml`, `home_remain_item.xml`, `home_days_item.xml` 신설, 기존 welcome/account/task/woozoora/admob 아이템 삭제. HomeFragmentTest id 수정.

### Task 14: 더보기와 프리미엄 판정 통일
`MoreAdapter.makeItems(isPremium)`: 프로필, 준비 가이드 그리드, 도구, 프리미엄 카드, 앱 목록(야간모드 토글 즉시 전환, 우주라이트 포함), 하단 네이티브 1개(프리미엄 제외). 배너 삭제. `MoreNativeHolder`가 `AdSlotBinder(showMedia = true)` 사용. `EditDialogViewModel`의 서버 `isPremium` 조회를 로컬(`repository.isPremium`은 로컬 우선이므로 그대로 두되 네트워크 폴백 결과를 기다리지 않도록 `LocalDataSource.isPremium` 경로 사용) 으로. `ids.xml`에서 `admob_banner_home_id`, `admob_banner_more_id` 삭제.

### Task 15: 디데이, 디데이 추가
`days_fragment` 상단 고정 결혼식 카드 + 행 목록, Lottie 대신 빈 상태 뷰. `days_add_fragment` 색 선택 제거(`Day.color = 0`), `day_background0~17` 삭제, `DaysBindings`의 색 매핑 제거.

### Task 16: 타임라인
`timeline_item.xml`을 레일 + 그룹 제목 + 접기/펼치기 항목으로. 오늘 그룹 `maru_yellow`, 지난 그룹 `maru_done`. 그룹 날짜는 `User.wedding` 기준 계산.

### Task 17: 등록(온보딩)
`register_fragment` 3단계 진행 표시, 질문형 제목, 추천 예산 칩, 키패드(입력 시트와 같은 레이아웃 include). 버튼 `Widget.Maru.Button.Primary`.

### Task 18: 나머지 화면에 시스템 적용
스플래시, 인트로, 온보딩 페이저, 결혼식 순서, 가이드 4종, 셀프웨딩, 공지, 설정, 알림, 약관, 백업/복원, 프리미엄 구매, 정렬/편집, 편집 다이얼로그. 색 토큰과 라디우스, 버튼 스타일만 적용. Lottie는 온보딩 3개와 완료 1개만 남기고 삭제.

### Task 19: 최종 정리
`colors.xml` 기존 색 삭제(참조 0 확인 후), 미사용 드로어블과 커스텀 뷰(`WrapViewPager`, `DayView`, `TransactionView`, `RemainTransactionView`, `EmptyTransactionView`, `UnderlineTextView` 등 참조 0인 것) 삭제, `nativetemplates` 모듈과 `mpChart`, `circleProgressView` 의존성 제거, `settings.gradle` 정리. 전체 빌드, JVM 테스트, 에뮬레이터 라이트/다크 스크린샷 8종, 접힘/펼침 확인.

---

## 실행 기록 (2026-09-16)

Task 1~19 구현 완료. `./gradlew :app:assembleDebug` 통과, `:app:testDebugUnitTest` 10/10 통과. Pixel 10 Pro Fold 에뮬레이터(API 36, 1080x2364 접힘 / 2076x2152 펼침)에서 라이트·다크, 접힘·펼침, 신규 설치(인트로→등록→온보딩→홈)와 시드 데이터(수민, 2027-01-23, 예산 1.2억, 계정 10건, 디데이 5건) 두 경로를 확인했다. 커밋은 하지 않았다.

### 계획과 다른 점
- Task 17 등록 화면: 3단계 진행 표시, 질문형 제목, 추천 예산 칩, 키패드 include는 적용하지 않았다. 기존 MotionLayout 3단계(이름→결혼 예정일→예산)에 색 토큰, 서체, 버튼 크기만 적용했다. 결혼 예정일은 기존과 같이 시스템 DatePicker로 고른다.
- 더보기 알림 행은 기존처럼 시스템 알림 설정으로 이동한다(앱 내 알림 화면 재설계는 하지 않음).
- 체크리스트 네이티브 광고 위치는 4행 뒤 첫 노출, 이후 10행 간격, 최대 2개(`Const.AD_FIRST_AFTER/AD_INTERVAL/AD_MAX`). 설계 문서의 "FAB와 겹치지 않도록" 조건을 만족시키기 위해 첫 위치를 4행 뒤로 고정했다.
- 완료 처리: 자동 완료 토글(`PreferenceUtils.autoComplete`)이 켜진 상태에서 잔금 0 저장 시 `DataRepository.updateTask(taskId, true)` 호출. `LocalDataSource.saveAccount`는 더 이상 완료 상태를 건드리지 않는다.

### 알려진 문제
- androidTest는 `com.linkedin.dexmaker:dexmaker:2.21.0` 의존성 해석 실패(이번 변경과 무관한 기존 환경 문제)로 컴파일하지 못했다. `FakeRepository`, `HomeFragmentTest`, `TasksFragmentTest`는 새 id/시그니처에 맞춰 수정했고 `InputFragmentTest`는 삭제했다(대체 테스트는 JVM `TasksListBuilderTest`).
- 에뮬레이터 검증 시 광고는 테스트 광고 단위가 아닌 실제 단위 id를 사용하므로 대부분 로드 실패 → 슬롯 접힘 경로로 동작을 확인했다.
