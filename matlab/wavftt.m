fs=44100;                  %�����źŲ���Ƶ��Ϊ8000  44100

x=wavread('D:\\03.wav',[20 532]);
x1=x(:,1); % ��ȡ�� 1 ����
x2=x(:,2); % ��ȡ�� 2 ����

t=(0:length(x1)-1)/fs;


FFTn=512
y1=fft(x1,FFTn);           %���ź���2048��FFT�任

f=fs*(0:(FFTn/2-1))/(FFTn);

figure(1)

plot(t,x1)                   %��ԭʼ�����źŵ�ʱ��ͼ��

grid on;axis tight;

title('ԭʼ�����ź�');

xlabel('time(s)');

ylabel('����');



figure(2)

xiang=abs(y1(1:FFTn/2))
plot(f(1:30),xiang(1:30))       %��ԭʼ�����źŵ�FFTƵ��ͼ

grid on;axis tight;

title('ԭʼ�����ź�FFTƵ��')

xlabel('Hz');

ylabel('����');