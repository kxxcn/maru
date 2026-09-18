from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageFilter


ROOT = Path('/Users/mk-am16-013/Dev/apps/maru')
OUT = ROOT / 'store_assets' / '2026-09-v2'
BG = OUT / 'maru-editorial-background-v2.png'

W, H = 1080, 1920
INK = '#20242B'
MUTED = '#626A75'
YELLOW = '#FFD86A'
IVORY = '#FCFBF7'


def font(size: int, bold: bool = False):
    path = '/System/Library/Fonts/AppleSDGothicNeo.ttc'
    return ImageFont.truetype(path, size=size, index=1 if bold else 0)


def rounded_mask(size, radius):
    mask = Image.new('L', size, 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, size[0] - 1, size[1] - 1), radius, fill=255)
    return mask


def rounded_image(image, size, radius):
    image = image.convert('RGB').resize(size, Image.Resampling.LANCZOS)
    image.putalpha(rounded_mask(size, radius))
    return image


def draw_spaced_text(draw, text, x, y, font_obj, color, tracking=0):
    cursor = x
    for char in text:
        draw.text((cursor, y), char, font=font_obj, fill=color)
        cursor += draw.textlength(char, font=font_obj) + tracking
    return cursor


def text_block(draw, lines, x, y, size, leading=1.12, color=INK, bold=True, tracking=1):
    f = font(size, bold)
    cursor = y
    for line in lines:
        draw_spaced_text(draw, line, x, cursor, f, color, tracking)
        bbox = draw.textbbox((0, 0), line, font=f)
        cursor += int((bbox[3] - bbox[1]) * leading)
    return cursor


def add_shadow(canvas, box, radius=42):
    shadow = Image.new('RGBA', canvas.size, (0, 0, 0, 0))
    sd = ImageDraw.Draw(shadow)
    x, y, w, h = box
    sd.rounded_rectangle((x + 0, y + 20, x + w, y + h + 20), radius, fill=(32, 36, 43, 45))
    shadow = shadow.filter(ImageFilter.GaussianBlur(24))
    canvas.alpha_composite(shadow)


def base_page(index):
    bg = Image.open(BG).convert('RGB').resize((W, H), Image.Resampling.LANCZOS)
    # Keep the generated edge illustration visible, but give copy and UI a quiet paper field.
    canvas = Image.new('RGBA', (W, H), IVORY)
    canvas.alpha_composite(bg.convert('RGBA'))
    veil = Image.new('RGBA', (W, H), (252, 251, 247, 158))
    canvas.alpha_composite(veil)
    draw = ImageDraw.Draw(canvas)
    return canvas, draw


def compose(index, source_path, kicker, title_lines, note, accent=YELLOW, screen_y=360):
    canvas, draw = base_page(index)
    draw_spaced_text(draw, kicker, 68, 124, font(27, True), MUTED, tracking=1)
    title_bottom = text_block(draw, title_lines, 68, 176, 62, leading=1.22, bold=True, tracking=1)
    draw_spaced_text(draw, note, 70, title_bottom + 10, font(26, False), MUTED, tracking=0.5)

    card_x, card_y, card_w, card_h = 195, max(screen_y, title_bottom + 90), 690, 1360
    add_shadow(canvas, (card_x, card_y, card_w, card_h), 48)
    draw = ImageDraw.Draw(canvas)
    draw.rounded_rectangle((card_x, card_y, card_x + card_w, card_y + card_h), 48, fill=(255, 255, 255, 245))

    source = Image.open(source_path).convert('RGB')
    max_w, max_h = 590, card_h - 26
    scale = min(max_w / source.width, max_h / source.height)
    sw, sh = int(source.width * scale), int(source.height * scale)
    shot = rounded_image(source, (sw, sh), 34)
    shot_x = card_x + (card_w - sw) // 2
    shot_y = card_y + (card_h - sh) // 2
    canvas.alpha_composite(shot, (shot_x, shot_y))

    return canvas.convert('RGB')


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    pages = [
        (1, '/tmp/maru-home.png', 'MARU WEDDING PLANNER', ['결혼 준비의 모든 순간을', '한 곳에서'], '체크리스트부터 예산, 디데이까지 차근차근.', YELLOW),
        (2, '/tmp/maru-tasks.png', 'CHECKLIST', ['결혼 준비,', '차근차근 하나씩.'], '해야 할 일을 한눈에 보고, 오늘의 준비를 시작하세요.', '#AFC8F5'),
        (3, '/tmp/maru-sort.png', 'MY CHECKLIST', ['내 순서에 맞게', '정리해보세요.'], '필요한 항목만 골라 나만의 체크리스트를 만들어요.', '#FFDCCF'),
        (4, '/tmp/maru-budget.png', 'BUDGET', ['예산도,', '함께 기록해요.'], '두 사람의 지출을 쉽고 투명하게 관리하세요.', '#FFE9A8'),
        (5, '/tmp/maru-days.png', 'D-DAY', ['기다리는 날을', '디데이로 만나보세요.'], '소중한 날짜를 기록하고 매일의 설렘을 확인해요.', '#B9D5B2'),
        (6, '/tmp/maru-current.png', 'MORE TOOLS', ['준비에 필요한 도구를', '한눈에.'], '결혼 준비에 필요한 가이드와 도구를 모았어요.', '#C6B8ED'),
        (7, '/tmp/maru-home.png', 'A LITTLE EVERY DAY', ['오늘의 준비가', '우리의 하루가 되도록.'], '마루와 함께 결혼 준비를 더 가볍게 시작하세요.', '#FFD86A'),
    ]
    for index, source, kicker, title, note, accent in pages:
        output = OUT / f'{index:02d}_maru_store.png'
        compose(index, source, kicker, title, note, accent).save(output, optimize=True)
    print(f'created {len(pages)} images in {OUT}')


if __name__ == '__main__':
    main()
