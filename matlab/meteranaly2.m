% 仪表指针识别

clear;
clc;
close all;
RGB=imread('1.jpg');
figure,imshow(RGB);     title('RGB')
GRAY=rgb2gray(RGB);
figure,imshow(GRAY);    title('GRAY')
threshold=graythresh(GRAY);
BW=im2bw(GRAY,threshold);
figure,imshow(BW);      title('BW')
BW=~BW;
figure,imshow(BW);      title('~BW')
BW=bwmorph(BW,'thin',Inf);
figure,imshow(BW);      title('BWMORPH')
[M,N]=size(BW);
[H,T,R] = hough(BW);
figure;
imshow(H,[],'XData',T,'YData',R,'InitialMagnification','fit');
       xlabel('\theta'), ylabel('\rho');
       axis on, axis normal, hold on;
       P  = houghpeaks(H,1,'threshold',ceil(0.3*max(H(:))));
       x = T(P(:,2)); 
       y = R(P(:,1));
       plot(x,y,'s','color','white');
%%%%%%%%%%%%%%%%%%%% Find lines and plot them%%%%%%%%%%%%%%
       lines = houghlines(BW,T,R,P,'FillGap',5,'MinLength',7);
       hold on;
        figure, imshow(RGB), hold on
       max_len = 0;
       for k = 1:length(lines)
         xy = [lines(k).point1; lines(k).point2];
         plot(xy(:,1),xy(:,2),'LineWidth',2,'Color','green');
%%%%%%%%%% plot beginnings and ends of lines%%%%%%%%%%%%%%%%%%
         plot(xy(1,1),xy(1,2),'x','LineWidth',2,'Color','yellow'); plot(xy(2,1),xy(2,2),'x','LineWidth',2,'Color','red');
%%%% determine the endpoints of the longest line segment %%%%
         len = norm(lines(k).point1 - lines(k).point2);
         if ( len > max_len)
           max_len = len;
           xy_long = xy;
         end
       end
%%%%%%%%%%%%% highlight the longest line segment%%%%%%%%%%%%%%
       plot(xy_long(:,1),xy_long(:,2),'LineWidth',2,'Color','cyan');
       k=(xy(2,2)-xy(1,2))/(xy(2,1)-xy(1,1));
     theta=pi/2+atan(k);
       if((xy(1,1)+xy(2,1))/2<=N/2)
               q=(theta+pi)*180/3.14;        
       else
           q=theta*180/3.14;             
       end
           shishu=q*6/2700-0.2;
       disp (theta);
       disp (q);
       disp (shishu); 
 
j=imread('1.jpg');
x=RGB2gray(j);
subplot(1,2,1);
imshow(x);
title('原图像');
 
f=double(x);
[m,n]=size(f);
 
h=fspecial('gaussian',[25,25],80);%创建高斯模板
q=imfilter(f,h,'same');
 
s=log(f+0.03)-log(q+0.03);
 
r=exp(s);
 
%归一化处理
max_r=max(r(:))*0.27;
min_r=min(r(:));
r=(r-min_r)/(max_r-min_r);
index=find(r>1);
r(index)=1;
R=mat2gray(r);
subplot(1,2,2);
imshow(R);
title('处理后的图像');
G=im2bw(R,0.7);
imshow(G);
I=uint8(G);
bw=edge(I,'sobel');
imshow(bw);

A=imread('1.jpg');
   I=rgb2gray(A);
  
T=0.5*(double(min(I(:)))+double(max(I(:))));
done=false;
while ~done
 
g=I>=T;
Tnext=0.5*(mean(I(g))+mean(I(~g)));
done=abs(T-Tnext)<0.5;
T=Tnext;
end
J=I;
K=find(J>=T);
J(K)=255;
K=find(J<T);
J(K)=0;
figure;
subplot(1,2,1),imshow(I,[]),title('原始图像');
subplot(1,2,2),imshow(J,[]),title('分割后图像');

t1=clock;
I=imread('1.jpg');
subplot(1,2,1);
J=rgb2gray(I);
title('pso算法图像分割的结果');
[a,b]=size(J);
[p,x]=imhist(J,256);
L=x';
LP=p'/(a*b);
n=256;
c1=2;
c2=2;
wmax=0.9;
wmin=0.4;
G=10;
M=15;
X=min(L)+fix((max(L)-min(L))*rand(1,M));
V=min(L)+(max(L)-min(L))*rand(1,M);
m=0;
for i=1:1:n
    m=m+L(i)*LP(i);
    endpbest=zeros(M,2);
    gbest1=0;
    gbest2=0;
    GG=0;
    t2=clock;
    for k=1:1:G
        w(k)=wmax-(wmax-wmin)*k/G;
        for i=1:1:M
            t=length(find(X(i)>=L));
            r=0;
            s=0;
            for j=1:1:t
                r=r+LP(j);
                s=s+L(j)*LP(j);
            end
            W0(i)=r;
            W1(i)=1-r;
            U0(i)=s/r;
            U1(i)=(m-s)/(1-r);
        end
        for i0=1:1:M
            BB(i0)=W0(i0)*W1(i0)*((U1(i0)-U0(i0))^2);
        end
        for i=1:1:M
            if pbest(i,2)<BB(i)
                pbest(i,2)=BB(i);
                pbest(i,1)=X(i);
            end
        end
        [MAX,CC]=max(BB);
        if MAX>=gbest2
            gbest2=MAX;
            gbest1=X(CC);
        end
        GG(k)=gbest2;
        for i=1:1:M
            V(i)=round(w(k)*V(i)+c1*rand*(pbest(i,1)-X(i))+c2*rand*(gbest1-X(i)));
            X(i)=V(i)+X(i);
        end
    end
    for i=1:1:a
        for j=1:1:b
            if J(i,j)>gbest1
                J(i,j)=250;
            else
                J(i,j)=0;
            end
        end
    end
    kk=1:1:G;
    gbest1;
    figure(1);
    imshow(J);
    figure(2);
    plot(kk,GG)
    tt=etime(clock,t1);
end

I=imread('1.jpg');
tmin=min(I(:));
tmax=max(I(:));
th=(tmin+tmax)/2;
ok=true;
while ok
    g1=I>=th;
    g2=I<th;
    u1=mean(I(g1));
    u2=mean(I(g2));
    tnew=(u1+u2)/2;
    if abs(th-tnew)<1  
        ok=0;
    end
end
th=tnew;
th=floor(th);
Inew=im2bw(I,th/255);
subplot(1,2,1)
imshow(I);
title('原始图像');
subplot(1,2,2)
imshow(Inew);
t=['迭代法分割后的图像，阈值=' num2str(th)];
title(t);
A0=imread('1.jpg');
seed=[100,220];
thresh=15;%相似性选择阈值
A=rgb2gray(A0);
A=imadjust(A,[min(min(double(A)))/255,max(max(double(A)))/255],[]);
A=double(A); 
B=A;
[r,c]=size(B);
n=r*c;
pixel_seed=A(seed(1),seed(2));
q=[seed(1) seed(2)];
top=1;
M=zeros(r,c);
M(seed(1),seed(2))=1;
count=1;
 
while top~=0;
    r1=q(1,1);
    c1=q(1,2);
    p=A(r1,c1);
    dge=0;
    for i=-1:1
        for j=-1:1
            if r1+i<=r & r1+i>0 & c1+j<=c & c1+j>0
                if abs(A(r1+i,c1+j)-p)<=thresh & M(r1+i,c1+j)~=1
                    top=top+1;
                    q(top,:)=[r1+i c1+j];
                    M(r1+i,c1+j)=1;
                    count=count+1;
                    B(r1+i,c1+j)=1;
                end
                    if M(r1+i,c1+j)==0;
                        dge=1;
                    end
            else
                            dge=1;
            end
        end
    end
    if dge~=1
        B(r1,c1)=A(seed(1),seed(2));
    end
        if count>=n
            top=1;
        end
            q=q(2:top,:);
            top=top-1;
end
subplot(1,2,1),imshow(A,[]);
subplot(1,2,2),imshow(B,[]);

A0=imread('1.jpg');
seed=[100,220];
thresh=15;%相似性选择阈值
A=rgb2gray(A0);
A=imadjust(A,[min(min(double(A)))/255,max(max(double(A)))/255],[]);
A=double(A); 
B=A;
[r,c]=size(B);
n=r*c;
pixel_seed=A(seed(1),seed(2));
q=[seed(1) seed(2)];
top=1;
M=zeros(r,c);
M(seed(1),seed(2))=1;
count=1;
 
while top~=0;
    r1=q(1,1);
    c1=q(1,2);
    p=A(r1,c1);
    dge=0;
    for i=-1:1
        for j=-1:1
            if r1+i<=r & r1+i>0 & c1+j<=c & c1+j>0
                if abs(A(r1+i,c1+j)-p)<=thresh & M(r1+i,c1+j)~=1
                    top=top+1;
                    q(top,:)=[r1+i c1+j];
                    M(r1+i,c1+j)=1;
                    count=count+1;
                    B(r1+i,c1+j)=1;
                end
                    if M(r1+i,c1+j)==0;
                        dge=1;
                    end
            else
                            dge=1;
            end
        end
    end
    if dge~=1
        B(r1,c1)=A(seed(1),seed(2));
    end
        if count>=n
            top=1;
        end
            q=q(2:top,:);
            top=top-1;
end
subplot(1,2,1),imshow(A,[]);
subplot(1,2,2),imshow(B,[]);

A0=imread('1.jpg');
seed=[100,220];
thresh=15;%相似性选择阈值
A=rgb2gray(A0);
A=imadjust(A,[min(min(double(A)))/255,max(max(double(A)))/255],[]);
A=double(A); 
B=A;
[r,c]=size(B);
n=r*c;
pixel_seed=A(seed(1),seed(2));
q=[seed(1) seed(2)];
top=1;
M=zeros(r,c);
M(seed(1),seed(2))=1;
count=1;
 
while top~=0;
    r1=q(1,1);
    c1=q(1,2);
    p=A(r1,c1);
    dge=0;
    for i=-1:1
        for j=-1:1
            if r1+i<=r & r1+i>0 & c1+j<=c & c1+j>0
                if abs(A(r1+i,c1+j)-p)<=thresh & M(r1+i,c1+j)~=1
                    top=top+1;
                    q(top,:)=[r1+i c1+j];
                    M(r1+i,c1+j)=1;
                    count=count+1;
                    B(r1+i,c1+j)=1;
                end
                    if M(r1+i,c1+j)==0;
                        dge=1;
                    end
            else
                            dge=1;
            end
        end
    end
    if dge~=1
        B(r1,c1)=A(seed(1),seed(2));
    end
        if count>=n
            top=1;
        end
            q=q(2:top,:);
            top=top-1;
end
subplot(1,2,1),imshow(A,[]);
subplot(1,2,2),imshow(B,[]);

A0=imread('1.jpg');
seed=[100,220];
thresh=15;%相似性选择阈值
A=rgb2gray(A0);
A=imadjust(A,[min(min(double(A)))/255,max(max(double(A)))/255],[]);
A=double(A); 
B=A;
[r,c]=size(B);
n=r*c;
pixel_seed=A(seed(1),seed(2));
q=[seed(1) seed(2)];
top=1;
M=zeros(r,c);
M(seed(1),seed(2))=1;
count=1;
 
while top~=0;
    r1=q(1,1);
    c1=q(1,2);
    p=A(r1,c1);
    dge=0;
    for i=-1:1
        for j=-1:1
            if r1+i<=r & r1+i>0 & c1+j<=c & c1+j>0
                if abs(A(r1+i,c1+j)-p)<=thresh & M(r1+i,c1+j)~=1
                    top=top+1;
                    q(top,:)=[r1+i c1+j];
                    M(r1+i,c1+j)=1;
                    count=count+1;
                    B(r1+i,c1+j)=1;
                end
                    if M(r1+i,c1+j)==0;
                        dge=1;
                    end
            else
                            dge=1;
            end
        end
    end
    if dge~=1
        B(r1,c1)=A(seed(1),seed(2));
    end
        if count>=n
            top=1;
        end
            q=q(2:top,:);
            top=top-1;
end
subplot(1,2,1),imshow(A,[]);
subplot(1,2,2),imshow(B,[]);
