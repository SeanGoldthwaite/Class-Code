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
t[,1] = floor(t[,1]/10) #turns age into decades
t = t[!(is.na(t[,][,2])),] #removes NAs from education
print(t)

conditional_prob = array(0,dim=c(7,3,2), dimnames=list(c("10s", "20s", "30s", "40s", "50s", "60s", "70s"), c("P", "S", "PS"), c("Y","N"))); # Q[vote, age, education] is P(x = v | class c)
len = length(t[,1])

educations = list("P", "S", "PS")
votes = list("Y", "N")

for (vote in 1:2) {
  for (dec in 1:7) {
    for (edu in 1:3) {
      conditional_prob[dec,edu,vote] = sum(t[,][,1] == dec & t[,][,2] == educations[edu] & t[,][,3] == votes[vote])/len
    }
  }
}
print(conditional_prob)

prior = c((863/len), (887/len))

c_head = array(0,dim=c(7,3,1), dimnames = list(c("10s", "20s", "30s", "40s", "50s", "60s", "70s"), c("P", "S", "PS"), c("Prediction")))

for (dec in 1:7) {
  for (edu in 1:3) {
    c_head[dec,edu,] = which.max(conditional_prob[dec,edu,] * prior)
  }
}
for (dec in 1:7) {
  for (edu in 1:3) {
    if (c_head[dec,edu,] == 1)
      c_head[dec,edu,] = 'Y'
    else
      c_head[dec,edu,] = 'N'
  }
}
print(c_head)