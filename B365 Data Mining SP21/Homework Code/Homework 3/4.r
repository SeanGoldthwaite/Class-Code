setwd("D:\\Classes\\B365\\Data")
x = read.csv2("./chilean_voting.csv",stringsAsFactors=FALSE,sep=",");

yes = (x[,9] == "Y" & !(is.na(x[,9])))   # boolean vector giving "Yes" voters who are not missing
no = (x[,9] == "N" & !(is.na(x[,9])))

table = x[yes | no,] #isolates only yes and no votes

table = table[,c(4,9)]
len = length(table[,1])
print(len)

print(table)

yesnum = 0
nonum = 0

femaleyes = 0
femaleno = 0

maleyes = 0
maleno = 0

for (i in 1:1757) {
  if(!(is.na(table[i,][,2]))) {
    if (table[i,][,2] == "Y") {
      yesnum = yesnum + 1
      if (table[i,][,1] == "F") {
        femaleyes = femaleyes + 1
      } else {
        maleyes = maleyes + 1
      }
    } else {
      nonum = nonum + 1
      if (table[i,][,1] == "F") {
        femaleno = femaleno + 1
      } else {
        maleno = maleno + 1
      }
    }
  }
}

print(yesnum)
print(nonum)

print(femaleyes)
print(femaleno)

print(maleyes)
print(maleno)