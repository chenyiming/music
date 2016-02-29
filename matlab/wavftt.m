fs=44100;                  %�����źŲ���Ƶ��Ϊ8000  44100

x=wavread('D:\\output.wav');
x1=x(:,1); % ��ȡ�� 1 ����
x2=x(:,2); % ��ȡ�� 2 ����

t=(0:length(x1)-1)/fs;



y1=fft(x1,2048*20);           %���ź���2048��FFT�任

f=fs*(0:1023)/(2048*20);

figure(1)

plot(t,x1)                   %��ԭʼ�����źŵ�ʱ��ͼ��

grid on;axis tight;

title('ԭʼ�����ź�');

xlabel('time(s)');

ylabel('����');

figure(2)

plot(f,abs(y1(1:1024)))       %��ԭʼ�����źŵ�FFTƵ��ͼ

grid on;axis tight;

title('ԭʼ�����ź�FFTƵ��')

xlabel('Hz');

ylabel('����');