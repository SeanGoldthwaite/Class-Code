setwd("D:\\Classes\\B365\\Homework Code\\Homework 3")
x = read.csv2("./chilean_voting.csv",stringsAsFactors=FALSE,sep=",");
#column labels
num = 1
region = 2
population = 3
sex = 4
age = 5
education = 6
income = 7
statusquo = 8
vote = 9

yes = (x[,vote] == "Y" & !(is.na(x[,vote])))   # boolean vector giving "Yes" voters who are not missing
no = (x[,vote] == "N" & !(is.na(x[,vote])))

table = x[yes | no,] #isolates only yes and no votes

t = table[,c(age, education, vote)] #isolates the age, education, and vote attributes
t[,1] = floor(t[,1]/10) #turns age into decades lived
t = t[!(is.na(t[,][,2])),] #removes NAs from education

print(t)