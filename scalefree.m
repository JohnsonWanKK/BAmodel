close all;
clear all;
fileID = fopen('degree.txt');
A = fscanf(fileID, '%d');
length = size(A);
for z =1:length
  y(z)=0;
end
C = max(A);
x =1:length;
for i = 1:length;
    for j = 1:C;
    if A(i)==x(j);
     y(j)=y(j)+1;
    end
    end
end
y1=(y/1000);
logy=log(y1);
logx=log(x);
logC = log(length);
figure;
plot(logx,logy);
title('1000 nodes');
ylabel('log P(k)');
xlabel('log k');
%xlim([log(2) logC]);
hold on;
