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
font_size = 16
size = (16, 16)
N = 100
chars = "A" # chars = string.ascii_uppercase

angle   = (-5., 5.) # (min, max) degres
scale_x = (0.9, 1.1) 
scale_y = (0.9, 1.1)  

# filters = [ BLUR, SMOOTH_MORE, SHARPEN, None ] # , EMBOSS ]
filters = [None] # disabled filtering

bgcolor = "rgb(255, 255, 255)"
fgcolor = "rgb(0, 0, 0)"

font = ImageFont.truetype("arial.ttf", font_size )

shutil.rmtree(dir, ignore_errors = True);
os.mkdir(dir)
for c in chars:
    os.mkdir(dir + '/' + c)
    for n in range(N):
        im = Image.new("RGBA", size, color = bgcolor)
        draw = ImageDraw.Draw(im)

        font_size = font.getsize(c)
        tx, ty = (size[0] - font_size[0]) / 2, \
                 (size[1] - font_size[1]) / 2

        # Печать текста
        draw.text((tx, ty), c, font = font, fill = fgcolor)

        # Вращение
        im = im.rotate(uniform(angle[0], angle[1]))

        # Масштабирование
        scx = uniform(size[0] * scale_x[0], size[0] * scale_x[1])
        scy = uniform(size[1] * scale_y[0], size[0] * scale_y[1])


        im = im.transform(size, Image.EXTENT, (size[0] - scx, 
                                               size[1] - scy, scx, scy) )

        # Обрезаем черные углы от вращения
        im_out = Image.new("RGBA", size, color = bgcolor)
        im_out.paste(im, None, im)

        # Фильтрация
        f = choice(filters)
        if f:
            im_out = im_out.filter(f)

        # Сохранение
        im_out.save(dir + '/' + c + "/" + str(n) + ".png")
