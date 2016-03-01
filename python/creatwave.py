__author__ = 'chenke'
# -*- coding: utf-8 -*-

import wave      #需要导入python的wave module，函数用法参http://docs.python.org/library/wave.html
import struct      #struct module的用法参见http://docs.python.org/library/struct.html
from math import *

MAX_AMPLITUDE = 10000    #决定sin wave的音量
SAMPLE_RATE = 44100   #采样频率，由于人听觉在20到20千赫兹，由于Nyquist定律，一般44100（大于20千的两倍）的频率足够满足人耳，再高就浪费文件空间啦，这也是CD通常的采样频率。
DURATION_SEC = 4       #生成wav的时间为三秒
SAMPLE_LEN = SAMPLE_RATE * DURATION_SEC       # 乘一下就是要写多少个SAMPLE啦
filename = 'd:/output.wav'     #起个文件名哈，文件生成后就到这个文件夹找啦
print "Creating sound file:", filename
print "Sample rate:", SAMPLE_RATE
print "Duration (sec):", DURATION_SEC
print "# samples:", SAMPLE_LEN
wavefile = wave.open(filename, 'w')  # 'w'写文件，其他还有'r','rb','wb'啥的,详情google之
wavefile.setparams((2, 2, SAMPLE_RATE, 0, 'NONE', 'not compressed'))   # 设置下wave file的头文件
samples = []    #建一个tuple用来放好几个channel的
temp=-1
for i in range(SAMPLE_LEN):
    t = float(i) / SAMPLE_RATE  # t表示当下滴时间
    if (i*4/SAMPLE_RATE)!=temp:
        print(i*4/SAMPLE_RATE)
        temp=i*4/SAMPLE_RATE;
    beishu= 1.05946 **(i*4/SAMPLE_RATE)

    F0= 441 # *beishu  # 载波频率
    F02=2*441 # *beishu  # 载波频率
    F1=0.25  # 调制频率
    A1=MAX_AMPLITUDE# * cos(t *F1* 2 * pi)  # 调制振幅
    sample = A1 *sin(t *F0* 2 * pi) # 就根据sin wave的方程得到当下的amplitude啦，这里生成频率为256的音高哟，可以随便改。
    sample2 = A1 *sin(t *F02* 2 * pi) # 就根据sin wave的方程得到当下的amplitude啦，这里生成频率为256的音高哟，可以随便改。
    sample2 = A1 *( sin(t *F0* 2 * pi)*1 + sin(t *F02* 2 * pi)*1)/2 # 就根据sin wave的方程得到当下的amplitude啦，这里生成频率为256的音高哟，可以随便改。
    #print(sample)
    #print i, t,  sample     # show some generated values. comment out for speed.
    packed_sample = struct.pack('h', sample)  # 转换成16进制的string
    packed_sample2 = struct.pack('h', sample2)  # 转换成16进制的string
    samples.append(packed_sample2)  # append到samples，作为channel 1
    samples.append(packed_sample2)  # append一个一样的作为channel 2，要是append另一个频率的 packed_sample_2,就可以有和声效果啦
sample_str = ''.join(samples)   # 把samples里所有的值都convert到一个string上
wavefile.writeframes(sample_str)        # 终于要写waveframe啦！
wavefile.close()    #最后别忘了关掉文件，不然会出错滴
print "Done writing file."          #大告成功！