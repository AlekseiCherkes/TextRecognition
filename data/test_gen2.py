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

size = (20, 20)

angle_amplitude = 15
image_count = 31

bgcolor = "rgb(255, 255, 255)"

teaching_dir = "for_teaching/teaching_set"
dst_root = "bakbak"


def get_dirs(rootDir):
    for dir_name in os.listdir(teaching_dir):
        if (dir_name != '.svn') : yield dir_name
        
def get_files(rootDir):
    for dir_name in get_dirs(rootDir):
        path = os.path.join(rootDir, dir_name)
        for file_name in os.listdir(path):
            yield (file_name, dir_name)


            
def replicate_pictures(rootDir):

    if os.path.exists(dst_root):
        shutil.rmtree( dst_root, ignore_errors = True);
    os.mkdir(dst_root)

    for file_name, dir_name in get_files(rootDir):
        src_path = os.path.join(rootDir, dir_name, file_name)
        print 'SrcPath %s' % src_path
        
        srcImg = Image.open(src_path)
        
        for i in xrange(image_count):
            if image_count == 1:
                d_ang = 0
            else:
                d_ang = 2.0 * angle_amplitude / (image_count - 1)
                
            angle = -angle_amplitude + d_ang * i
                
            dstImg = srcImg.rotate(angle, resample=Image.BICUBIC)
            dst_name = "%s_%d.png" % (file_name, i)
            dir_path = os.path.join(dst_root, dir_name)
            dst_path = os.path.join(dir_path, dst_name)
            if not os.path.exists(dir_path):
                os.mkdir(dir_path)

            im_out = Image.new("RGBA", size, color = bgcolor)
            im_out.paste(dstImg, None, dstImg) # обрезаем черные углы от вращения
            im_out.save(dst_path)
            
            
        
replicate_pictures(teaching_dir)
    

