fs=44100;                  %�����źŲ���Ƶ��Ϊ8000  44100

x=wavread('D:\\01.wav');%,[fs*1 fs*2]);
x1=x(:,1); % ��ȡ�� 1 ����
%x2=x(:,2); % ��ȡ�� 2 ����

t=(0:length(x1)-1)/fs;%��������ʱ������
figure(1)
plot(t,x1)                   %��ԭʼ�����źŵ�ʱ��ͼ��
grid on;axis tight;
title('ԭʼ�����ź�');
xlabel('time(s)');
ylabel('����');



nFFT=4096  ;%���ź���xxx���FFT�任
y1=fft(x1,nFFT);           %���ź���2048��FFT�任
f=fs*(0:(nFFT/2-1))/(nFFT); %��������Ƶ������
xiang=abs(y1(1:nFFT/2));
figure(2)
plot(f(1:80),xiang(1:80) )       %��ԭʼ�����źŵ�FFTƵ��ͼ
grid on;axis tight;
title('ԭʼ�����ź�FFTƵ��')
xlabel('Hz');
ylabel('����');

figure(3)
% ��ʵMatlab�Դ���ʱƵ�����ĺ���~��������������ʱ��������~~
% [b,f,t]=specgram(data,nfft,Fs,window,numoverlap);
% imagesc(t,f,20*log10(abs(b))), axis xy, colormap(jet); % ��ʱƵͼ
% ���У�
% nfft��fft�ĳ��ȣ�Խ���Ļ���Ƶ��ֱ���Խ�ߣ����ǣ�������������ʱ���źţ����ܹ�����һ�������16k����8k��ȡ1024����512��Ҫ�������ʵĸߵ͡�
% fs���ǲ����ʣ�����˵�ˡ�
% window��ָ���ĳ��ȣ�һ���nfft��ͬ���ɡ�
% numoverlap��ָnfft��ȥ������Խ��Խ�ã���������Խ��һ��ȡnfft��3/4Ч���ͱȽϺ��ˡ�
% ������
% b= specgram(a,512,8000,512,384);
% b��һ�����󣬸����ģ���ͼ��ʱ����Ҫȡ����ֵ��
[b,f1,t1]=specgram(x1,nFFT,fs,nFFT,nFFT*15/16);
%abs1=abs(b(1:50,:));
imagesc(t1,f1(1:50), abs(b(1:50,:))), 
axis xy, 
colormap(jet); % ��ʱƵͼ

bx= (abs(b(:,1))) ; % ��ĳһ����������ƵƵͼ
bx(:,2)=abs(b(:,10));
figure(4);
plot(f1(1:80),bx(1:80,1),'DisplayName','f1 vs. bx','XDataSource','f1','YDataSource','bx');
