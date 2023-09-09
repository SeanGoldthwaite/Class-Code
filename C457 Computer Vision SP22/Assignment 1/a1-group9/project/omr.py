import enum
import os
from re import template
import sys
import edgedetect
import staffdetect
import cvlib
import templatematching as tm
import numpy as np
from PIL import Image, ImageDraw
import argparse
import random
import math
import util

note_threshold = 150
quarter_rest_threshold = 248
eighth_rest_threshold = 252
note_duplicate_distance = 2
note_scaling = 1
qrest_scaling = 2.9
erest_scaling = 2.49

class TestImage:
    def __init__(self, image, format='L'):
        self.path = image.filename
        self.image = image.convert(format)
        self.array = np.array(self.image)

# Collect templates
template_dir = os.path.join(os.path.dirname(__file__), '..', 'templates')
output_dir = os.path.join(os.path.dirname(__file__), '..', 'outputs')
do_output = True

class Template:
    def __init__(self, name, format='1'):
        self.path = os.path.join(template_dir, name)
        self.image = Image.open(self.path).convert(format)
        self.array = np.array(self.image)
    def show(self):
        self.image.show()
    def resize(self, new_width, new_height):
        resized = self.image.resize((new_width, new_height))
        self.image = resized
        self.array = np.array(resized)

class Page:
    def __init__(self, image):
        self.source = image
        self.source_array = image.array
        self.output_array = np.array(Image.fromarray(self.source_array).convert('RGB'))
        self.output = Image.fromarray(self.output_array)
        self.output_txt = ''
        self.notes = []
        self.quarter_rests = []
        self.eighth_rests = []
        self.stafflist = None
        self.pitch_map = {}
        self.confidence_map = {}
        self.pitch_text_offset = 10
        self.avg_staff_gap = -1

        self.note_template = template_note_filled
        self.quarter_template = template_quarter_rest
        self.eighth_template = template_eighth_rest
    
    def ocr_notes(self):
        note_mask = mask_from_template(self.source_array, template_note_filled.array)
        detected_notes = tm.mask_to_clusters(note_mask, 15, 75, self.note_template)
        self.notes.extend(detected_notes)
    
    def ocr_quarter_rests(self):
        quarter_mask = mask_from_template(self.source_array, template_quarter_rest.array)
        detected_quarters = filter_matches(quarter_mask, quarter_rest_threshold)
        self.quarter_rests.extend(detected_quarters)

    def ocr_eighth_rests(self):
        eighth_mask = mask_from_template(self.source_array, template_eighth_rest.array)
        detected_eighths = filter_matches(eighth_mask, eighth_rest_threshold)
        self.eighth_rests.extend(detected_eighths)

    def ocr_staffs(self):
        self.stafflist = process_image_staffs(self.source, False)
        self.avg_staff_gap = np.mean([staff.gap_size for staff in self.stafflist.staffs])
        self.resize_note_template(self.avg_staff_gap)
        #self.resize_quarter_template(self.avg_staff_gap)
        #self.resize_eighth_template(self.avg_staff_gap)

    def assign_note_pitches(self):
        stafflist = self.stafflist
        for pos in self.notes:
            staff, staff_c = stafflist.get_closest_staff(pos[0])
            index, index_c = staff.get_index_for_position(pos[0])
            confidence = staff_c * index_c
            pitch = staff.get_note_for_index(index) # String; e.g. 'C'
            self.confidence_map[pos] = confidence
            self.pitch_map[pos] = pitch
        self.clean_duplicate_notes()

        
    def ocr_all(self):
        self.ocr_staffs()
        self.ocr_notes()
        self.ocr_quarter_rests()
        self.ocr_eighth_rests()
        self.assign_note_pitches()

    def clean_duplicate_notes(self):
        print("Cleaning duplicate notes")
        # iterate through the notes
        note_groups = {'A':[], 'B':[], 'C':[], 'D':[], 'E':[], 'F':[], 'G':[]}
        # if a note is closer than the duplicate_distance to another note, and has the same pitch
        # choose the one with the higher confidence and discard the other
        for note in self.notes:
            pitch = self.pitch_map[note]
            note_groups[pitch].append(note)
            for o in note_groups[pitch]:
                if cvlib.euclidean_distance(o, note) < note_duplicate_distance*self.avg_staff_gap:
                    if self.confidence_map[note] > self.confidence_map[o]:
                        note_groups[pitch].remove(o)
                        note_groups[pitch].append(note)
                        print("Removed duplicate note")
                        print("Pitch is", pitch, "Note is", note)
        self.notes = sum(note_groups.values(), [])

    def render_notes(self):
        for m in self.notes:
            draw_box(self.output_array, self.note_template.array.shape, m[0], m[1])

    def render_quarter_rests(self):
        for m in self.quarter_rests:
            draw_box(self.output_array, self.quarter_template.array.shape, m[0], m[1], (0, 255, 0))

    def render_eighth_rests(self):
        for m in self.eighth_rests:
            draw_box(self.output_array, self.eighth_template.array.shape, m[0], m[1], (0, 0, 255))

    def render_pitches(self):
        drawing = ImageDraw.Draw(self.output)
        for m in self.notes:
            pitch = self.pitch_map[m]
            drawing.text((m[1]+self.pitch_text_offset, m[0]), pitch, (255,0,255))

    def render(self):
        self.output = Image.fromarray(self.output_array)
        self.render_pitches()

    def resize_note_template(self, staff_gap):
        new_height = note_scaling * staff_gap
        aspect_ratio = self.note_template.array.shape[1] / self.note_template.array.shape[0]
        new_width = int(new_height * aspect_ratio)
        self.note_template.resize(new_width, int(new_height))

    def resize_quarter_template(self, staff_gap):
        new_height = qrest_scaling * staff_gap
        aspect_ratio = self.quarter_template.array.shape[1] / self.quarter_template.array.shape[0]
        new_width = int(new_height * aspect_ratio)
        self.quarter_template.resize(new_width, int(new_height))

    def resize_eighth_template(self, staff_gap):
        new_height = erest_scaling * staff_gap
        aspect_ratio = self.eighth_template.array.shape[1] / self.eighth_template.array.shape[0]
        new_width = int(new_height * aspect_ratio)
        self.eighth_template.resize(new_width, int(new_height))

    def show(self):
        self.output.show()

    def save_txt(self):
        og_stdout = sys.stdout
        filename = os.path.split(self.source.path)[1]
        filename = filename[:-3] + 'txt'
        path = os.path.join(output_dir, filename)
        with open(path, 'w') as f:
            sys.stdout = f
            print(self.output_txt)
            sys.stdout = og_stdout

    def save_img(self):
        filename = os.path.split(self.source.path)[1]
        filename = filename[:-3] + 'png'
        path = os.path.join(output_dir, filename)
        self.output.save(path)

    def construct_txt(self):
        for note in self.notes:
            y, x = note
            pitch = self.pitch_map[note]
            confidence = self.confidence_map[note]
            template_y, template_x = template_note_filled.array.shape
            box_x = int(x - template_x / 2)
            box_y = int(y - template_y / 2)
            self.output_txt += f'<row={box_y}><col={box_x}><height={template_y}><width={template_x}><symbol_type=filled_note><pitch={pitch}><confidence={confidence}>\n'
        
        for qrest in self.quarter_rests:
            y, x = qrest
            template_y, template_x = template_quarter_rest.array.shape
            box_x = int(x - template_x / 2)
            box_y = int(y - template_y / 2)
            self.output_txt += f'<row={box_y}><col={box_x}><height={template_y}><width={template_x}><symbol_type=quarter_rest><pitch=_><confidence=0>\n'

        for erest in self.eighth_rests:
            y, x = erest
            template_y, template_x = template_eighth_rest.array.shape
            box_x = int(x - template_x / 2)
            box_y = int(y - template_y / 2)
            self.output_txt += f'<row={box_y}><col={box_x}><height={template_y}><width={template_x}><symbol_type=eighth_rest><pitch=_><confidence=0>\n'


template_note_filled = Template("template1.png")
template_quarter_rest = Template("template2.png")
template_eighth_rest = Template("template3.png")

# Show the given array as a regular image. Assumes the image has shape (W, H)
# and all values are in range [0, 255].
def show_array_as_image(img):
    im = Image.fromarray(img.astype(np.uint8))
    im.show()

# Show the given array as a normalmap image.
def show_array_as_normalmap(img):
    im = Image.fromarray((img * np.array([127, 127, 255]) + np.array([127, 127, 0])).astype(np.uint8), 'RGB')
    im.show()

NOTES = ['A', 'B', 'C', 'D', 'E', 'F', 'G']

class Staff:
    def __init__(self, midpoint, gap_size, first_note):
        self.midpoint = midpoint
        self.gap_size = gap_size
        self.first_note = first_note
    def get_index_for_position(self, ycenter):
        top_bar = self.midpoint + self.gap_size * 2
        index = (top_bar - ycenter) / (self.gap_size / 2)
        ret = int(np.rint(index))
        confidence = math.sqrt(1.0 - 2.0 * abs(ret - index))
        return (ret, confidence)
    def get_note_for_index(self, index):
        return NOTES[(index + self.first_note) % len(NOTES)]

class StaffList:
    def __init__(self, staffs, radius):
        self.staffs = staffs
        self.gap_size = radius/2
        self.radius = radius
    def get_closest_staff(self, ycenter):
        max_dis = math.inf
        ret = None
        for s in self.staffs:
            dis = abs(s.midpoint - ycenter)
            if dis < max_dis:
                max_dis = dis
                ret = s
        confidence = 1
        if max_dis > ret.gap_size * 3:
            # can probably be improved
            confidence = (s.gap_size * 3) / max_dis
        return (ret, confidence)

def process_image_staffs(image, do_display):
    # Apply sobel
    print("applying sobel...")
    convolved = edgedetect.apply_sobel_magnitude(image.array)
    # Create orientation map
    omap = edgedetect.apply_sobel_orientation(image.array)
    # Apply supression
    print("Applying non maximal supression...")
    supressed = edgedetect.supress_nonmaximals(convolved, omap)
    # Apply canny thresholding
    print("Applying Canny thresholding...")
    canny = edgedetect.apply_canny(supressed, 10, 35)
    # Convert to hough space
    print("Converting to Hough space...")
    hough = staffdetect.HoughSpaceStaff((image.array.shape[0], image.array.shape[0] // 2))
    for iy in range(image.array.shape[0]):
        s = np.sum(canny[iy,:]) / (255.0 * image.array.shape[1])
        s = math.pow(s, 1.0/2.0)
        y = iy / image.array.shape[0]
        hough.cast_vote(y, s)
    hough.image = cvlib.convolve(hough.image, np.array([1, 4, 6, 4, 1]).reshape((1, 5)) / 16.0, np.float64)
    hough.image = cvlib.convolve(hough.image, np.array([1, 4, 6, 4, 1]).reshape((5, 1)) / 16.0, np.float64)
    # Find the mode of sizes
    radius = hough.get_size_mode()
    # Blur based on mode
    gaussian = util.make_gaussian_1d(hough.get_size_index(radius) / 8)
    gaussian = gaussian.reshape((gaussian.shape[0], 1))
    hough.image = cvlib.convolve(hough.image, gaussian, np.float64)
    # Normalize
    hough.image = hough.get_normalized() * 255.0
    if do_display:
        show_array_as_image(hough.image)
    print("Finding islands...")
    # Get a 1D array of points with the given size
    points = hough.get_points_of_size(radius)
    # Supress non-maximals with a decently large dip threshold
    points = util.supress_nonmaximals_1d(points, 80)
    # Get points (which are likely to be staffs)
    staffpoints = [(x, radius) for x in util.get_points_1d(points, 150)]
    print("Found {} points".format(len(staffpoints)))
    print("Creating staffs...")
    if do_display:
        imageout = image.image.convert('RGB')
        imagedraw = ImageDraw.Draw(imageout, 'RGBA')
    x1 = 0
    x2 = image.array.shape[1]-1
    # Get actual centers for points
    staffpoints = [(hough.get_center_value(staff[0]), staff[1]) for staff in staffpoints]
    print("Got {} staffs...".format(len(staffpoints)))
    # Sort staffs
    staffpoints = sorted(staffpoints, key=lambda staff: staff[0])
    # Create stafflist
    retstaffs = []
    for i, staff in enumerate(staffpoints):
        center = staff[0]
        radius = staff[1]
        points = [center - radius * (i - 2) / 2 for i in range(5)]
        if do_display:
            for i, p in enumerate(points):
                color = (255, 0, 0, 100) if (i == 0 or i == 4) else (0, 255, 100, 100)
                thickness = 4 if (i == 0 or i == 4) else 2
                y = math.floor(p * image.array.shape[0])
                imagedraw.line(((x1, y), (x2, y)), color, width=thickness)
        retstaffs.append(Staff(
            points[2] * image.array.shape[0],
            radius * image.array.shape[0] / 2,
            4 if i % 2 == 0 else 6))
    if do_display:
        imageout.show()
    stafflist = StaffList(retstaffs, radius)
    print("Done!")
    return stafflist

def mask_from_template(imarray, temparray):
    mask = tm.compute_template_mask(imarray, temparray)
    mask = edgedetect.apply_canny(mask, 5, 15).astype(np.uint8)
    return mask

def filter_matches(imarray, threshold):
    matches = []
    for i in range(len(imarray)):
        for j in range(len(imarray[0])):
            if imarray[i][j] >= threshold:
                matches.append((i, j))
    return matches

def draw_pixel(imarray, y, x, color=(255, 0, 0)):
    if y < 0 or x < 0 or y >=  imarray.shape[0] or x >= imarray.shape[1]:
        return
    imarray[y][x] = color

def draw_box(imarray, box_size, y, x, color=(255, 0, 0)):
    if type(box_size) != tuple:
        box_size = (box_size, box_size)
    for i in range(box_size[0] * 2):
        draw_pixel(imarray, y-box_size[0]+i, x-box_size[1], color)
        draw_pixel(imarray, y+box_size[0]-i, x+box_size[1], color)
    for i in range(box_size[1] * 2):
        draw_pixel(imarray, y-box_size[0], x-box_size[1]+i, color)
        draw_pixel(imarray, y+box_size[0], x+box_size[1]-i, color)

def main():
    global output_dir
    global do_output
    parser = argparse.ArgumentParser(description="Music Sheet reader")
    parser.add_argument('input', type=Image.open, nargs='+', help="Input images to process")
    parser.add_argument('-d', '--display-results', help="Shows output as image viewer", action='store_true')

    group_output = parser.add_mutually_exclusive_group(required=False)
    group_output.add_argument('--out', help="Output folder", default='outputs')
    group_output.add_argument('--no-out', help="Do not output resulting data", action='store_true')

    group_action = parser.add_mutually_exclusive_group(required=False)
    group_action.add_argument('--only-detect-staffs', help="Only perform the edge detection step", action='store_true')
    group_action.add_argument('--only-detect-notes', help="Only perform the note detection step", action='store_true')
    group_action.add_argument('--only-detect-rests', help="Only perform the rest detection step", action='store_true')

    args = parser.parse_args()
    if args.out != None:
        output_dir = args.out
    do_output = not args.no_out

    if do_output:
        if not os.path.exists(output_dir):
            os.makedirs(output_dir)

    test_images = [TestImage(img) for img in args.input]

    # Collect test images
    if args.only_detect_staffs:
        for img in test_images:
            process_image_staffs(img, args.display_results)
        exit()

    for i, img in enumerate(test_images):
        music_sheet = Page(img)
        music_sheet.ocr_staffs()
        if not args.only_detect_rests:
            music_sheet.ocr_notes()
            music_sheet.assign_note_pitches()
            music_sheet.render_notes()
            music_sheet.render_pitches()
        if not args.only_detect_notes:
            music_sheet.ocr_eighth_rests()
            music_sheet.ocr_quarter_rests()
            music_sheet.render_eighth_rests()
            music_sheet.render_quarter_rests()
        music_sheet.render()
        music_sheet.construct_txt()
        if args.display_results:
            music_sheet.show()
        if do_output:
            music_sheet.save_txt()
            music_sheet.save_img()

    #  Detect all of the staves and the scale of the image
    #  (the space between staff lines is approximately the height of a notehead)

    # Rescale the image

    # Detect the notes and eighth and quarter rests in the image



    # Output two files:

    #   (a) detected.png: Visualization of which notes were detected (as in Fig 1(b)).

    #   (b) detected.txt: transcription of detection resuls to text format
    #   each line of detected.txt should follow this template:
    #   < row >< col >< height >< width >< symbol type >< pitch >< confidence >

if __name__ == "__main__":
    main()
