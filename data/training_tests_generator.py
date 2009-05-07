#-*- encoding:utf-8 -*-
import sys
sys.path.append("C:\Program Files\Python\Lib\site-packages\PIL")
import Image
import ImageDraw
import ImageFont
from ImageFilter import *
import string
import shutil
import os
from random import uniform, choice

teaching_dir = "for_teaching/teaching_set"
control_dir = "for_teaching/control_set"
font_size = 20
size = (20, 20)
teach_N = 1
control_N = 0
#chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" # chars = string.ascii_uppercase
chars = "ABCDEFGHIJKLM"

angle   = (-15., 15) # (min, max)
scale_x = (0.9, 1.1)
scale_y = (0.9, 1.1)

filters = [ BLUR, SMOOTH_MORE, SHARPEN ] # , EMBOSS ]

bgcolor = "rgb(255, 255, 255)"
fgcolor = "rgb(0, 0, 0)"

font = ImageFont.truetype("arial.ttf", font_size )




def symbol_generate( c ):
    ''' Generate modification of symbol 'c'.
        Return type: Image  '''
    im = Image.new("RGBA", size, color = bgcolor)
    draw = ImageDraw.Draw(im)

    font_size = font.getsize(c)
    tx, ty = (size[0] - font_size[0]) / 2, \
             (size[1] - font_size[1]) / 2

    draw.text((tx, ty), c, font = font, fill = fgcolor)

    im = im.rotate(uniform(angle[0], angle[1]))

    scx = uniform(size[0] * scale_x[0], size[0] * scale_x[1])
    scy = uniform(size[1] * scale_y[0], size[0] * scale_y[1])

    im = im.transform(size, Image.EXTENT, (size[0] - scx,
                                           size[1] - scy, scx, scy) )

    im_out = Image.new("RGBA", size, color = bgcolor)
    im_out.paste(im, None, im) # обрезаем черные углы от вращения
    #im_out = im_out.filter(choice(filters))
    return im_out


shutil.rmtree( teaching_dir, ignore_errors = True);
shutil.rmtree( control_dir, ignore_errors = True);
os.mkdir(teaching_dir)
os.mkdir(control_dir)
for c in chars:
    os.mkdir(teaching_dir + '/' + c)
    os.mkdir(control_dir + '/' + c)
    for n in range(teach_N):
        im_out = symbol_generate( c )
        im_out.save(teaching_dir + '/' + c + "/" + str(n) + ".png")
    for n in range(control_N):
        im_out = symbol_generate( c )
        im_out.save(control_dir + '/' + c + "/" + str(n) + ".png")
