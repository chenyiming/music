fs=44100;                  %语音信号采样频率为8000  44100

x=wavread('D:\\output.wav');
x1=x(:,1); % 抽取第 1 声道
x2=x(:,2); % 抽取第 2 声道

t=(0:length(x1)-1)/fs;



y1=fft(x1,2048*20);           %对信号做2048点FFT变换

f=fs*(0:1023)/(2048*20);

figure(1)

plot(t,x1)                   %做原始语音信号的时域图形

grid on;axis tight;

title('原始语音信号');

xlabel('time(s)');

ylabel('幅度');

figure(2)

plot(f,abs(y1(1:1024)))       %做原始语音信号的FFT频谱图

grid on;axis tight;

title('原始语音信号FFT频谱')

xlabel('Hz');

ylabel('幅度');