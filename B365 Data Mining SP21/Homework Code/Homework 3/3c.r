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

t = table[,c(sex, education, region, age, vote)] #isolates the age, education, and vote attributes
t[,4] = floor(t[,4]/10) #turns age into decades
t = t[!(is.na(t[,][,2])),] #removes NAs from education
print(t)

conditional_prob = array(0,dim=c(2,3,5,7,2), dimnames=list(c("F", "M"), c("P", "S", "PS"), c("N", "C", "S", "SA", "M"), c("10s", "20s", "30s", "40s", "50s", "60s", "70s"), c("Y","N"))); # Q[vote, age, education] is P(x = v | class c)
len = length(t[,1])

educations = list("P", "S", "PS")
votes = list("Y", "N")
sexes = list("F", "M")
regions = list("N", "C", "S", "SA", "M")

print(t[,][,1])
print(t[,][,2])
print(t[,][,3])
print(t[,][,4])
print(t[,][,5])

for (vote in 1:2) {
  for (sex in 1:2) {
    for (edu in 1:3) {
      for (reg in 1:5) {
        for (dec in 1:7) {
          conditional_prob[sex,edu,reg,sex,vote] = (sum(t[,][,1] == sexes[sex] & t[,][,5] == votes[vote])
                                            + sum(t[,][,2] == educations[edu] & t[,][,5] == votes[vote])
                                            + sum(t[,][,3] == regions[reg] & t[,][,5] == votes[vote])
                                            + sum(t[,][,4] == dec & t[,][,5] == votes[vote]))/len
        }
      }
    }
  }
}
#print(conditional_prob)

prior = c((863/len), (887/len))

c_head = array(0,dim=c(2,3,5,7,1), dimnames = list(c("F", "M"), c("P", "S", "PS"), c("N", "C", "S", "SA", "M"), c("10s", "20s", "30s", "40s", "50s", "60s", "70s"), c("Prediction")))

for (sex in 1:2) {
  for (edu in 1:3) {
    for(reg in 1:5) {
      for (dec in 1:7) {
        c_head[sex,edu,reg,dec,] = which.max(conditional_prob[sex,edu,reg,dec,] * prior)
      }
    }
  }
}
for (sex in 1:2) {
  for (edu in 1:3) {
    for (reg in 1:5) {
      for (dec in 1:7) {
        if (c_head[sex,edu,reg,dec,] == 1)
          c_head[sex,edu,reg,dec,] = 'Y'
        else
          c_head[sex,edu,reg,dec,] = 'N'
      }
    }
  }
}
#1 -> female
#3 -> PS education
#4 -> SA Region
#5 -> 50s
print(c_head[1,3,4,5,])