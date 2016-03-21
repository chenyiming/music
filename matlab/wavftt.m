fs=44100;                  %语音信号采样频率为8000  44100

x=wavread('D:\\01.wav');%,[fs*1 fs*2]);
x1=x(:,1); % 抽取第 1 声道
%x2=x(:,2); % 抽取第 2 声道

t=(0:length(x1)-1)/fs;%运算结果的时间序列
figure(1)
plot(t,x1)                   %做原始语音信号的时域图形
grid on;axis tight;
title('原始语音信号');
xlabel('time(s)');
ylabel('幅度');



nFFT=4096  ;%对信号做xxx点的FFT变换
y1=fft(x1,nFFT);           %对信号做2048点FFT变换
f=fs*(0:(nFFT/2-1))/(nFFT); %运算结果的频率序列
xiang=abs(y1(1:nFFT/2));
figure(2)
plot(f(1:80),xiang(1:80) )       %做原始语音信号的FFT频谱图
grid on;axis tight;
title('原始语音信号FFT频谱')
xlabel('Hz');
ylabel('幅度');

figure(3)
% 其实Matlab自带了时频分析的函数~在做语音分析的时候会很有用~~
% [b,f,t]=specgram(data,nfft,Fs,window,numoverlap);
% imagesc(t,f,20*log10(abs(b))), axis xy, colormap(jet); % 画时频图
% 其中：
% nfft是fft的长度，越长的话，频域分辨率越高，但是，对于语音这种时变信号，不能过长，一般采样率16k或者8k的取1024或者512，要看采样率的高低。
% fs就是采样率，不多说了。
% window是指窗的长度，一般和nfft相同即可。
% numoverlap是指nfft减去步长，越大越好，但运算量越大。一般取nfft的3/4效果就比较好了。
% 举例：
% b= specgram(a,512,8000,512,384);
% b是一个矩阵，复数的，画图的时候需要取绝对值！
[b,f1,t1]=specgram(x1,nFFT,fs,nFFT,nFFT*15/16);
%abs1=abs(b(1:50,:));
imagesc(t1,f1(1:50), abs(b(1:50,:))), 
axis xy, 
colormap(jet); % 画时频图

bx= (abs(b(:,1))) ; % 画某一个抽样窗的频频图
bx(:,2)=abs(b(:,10));
figure(4);
plot(f1(1:80),bx(1:80,1),'DisplayName','f1 vs. bx','XDataSource','f1','YDataSource','bx');
