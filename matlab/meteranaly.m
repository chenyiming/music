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
        figure, imshow(RGB),title('½á¹û'); hold on
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
 