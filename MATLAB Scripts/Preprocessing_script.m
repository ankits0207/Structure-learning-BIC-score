clc;
clear all;
close all;
source_dir = 'C:\Users\ankit\Desktop\Test_data_folder\Arity3\Input';
dest_dir = 'C:\Users\ankit\Desktop\Test_data_folder\Arity3\Output\';
source_files = dir(fullfile(source_dir, '*.xls'));
for i = 1:length(source_files)
  Complete_data = xlsread(fullfile(source_dir, source_files(i).name));
  Complete_data = log(Complete_data);
  Mean_of_data = mean(Complete_data);
  Standard_deviation = std(Complete_data);
  for j = 1:11
    Column = Complete_data(:,j);
    Mean = Mean_of_data(:,j);
    Std = Standard_deviation(:,j);
    for k = 1:size(Column,1)
        if((Mean - Std < Column(k,1)) && (Column(k,1) < Mean + Std))
            Complete_data(k,j) = 1;
        elseif (((Mean - 2*Std < Column(k,1)) && (Mean - Std > Column(k,1))) || ((Column(k,1) > Mean + Std) && (Column(k,1) < Mean + 2*Std)))
            Complete_data(k,j) = 2;
        else
            Complete_data(k,j) = 3;
        end
    end
  end
  xlswrite(fullfile(dest_dir, source_files(i).name), Complete_data);
end