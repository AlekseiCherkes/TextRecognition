#-*- encoding:utf-8 -*-
import Image
import ImageDraw
import ImageFont
from ImageFilter import *
import string
import shutil
import os
from random import uniform, choice

dir = "teaching_set"
font_size = 30
size = (32, 32)
N = 1
chars = "A" # chars = string.ascii_uppercase

angle   = (-15., 15) # (min, max)
scale_x = (0.9, 1.1) 
scale_y = (0.9, 1.1)  

filters = [ BLUR, SMOOTH_MORE, SHARPEN ] # , EMBOSS ]

bgcolor = "rgb(255, 255, 255)"
fgcolor = "rgb(0, 0, 0)"

font = ImageFont.truetype("arial.ttf", font_size )

shutil.rmtree("./dir", ignore_errors = True);
os.mkdir(dir)
for c in chars:
    os.mkdir(dir + '/' + c)
    for n in range(N):
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
        im_out = im_out.filter(choice(filters))
        im_out.save(dir + '/' + c + "/" + str(n) + ".png")
