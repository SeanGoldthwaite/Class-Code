pa = 0.5
pb = 0.25
pc = 0.25

#proves that dice roll functions are working correctly 
num = 1000000
suma = sum(dice_a(num))/num
sumb = sum(dice_b(num))/num
sumc = sum(dice_c(num))/num
print(suma) #expected 3.5
print(sumb) #expected 3.6666666...
print(sumc) #expected 4

dice = choose_dice(1)
results = switch(dice, "A" = dice_a(3), "B" = dice_b(3), "C" = dice_c(3))

print(dice)
print(results)

dice_a <- function(num_rolls) {
  return(sample(1:6, size = num_rolls, replace = TRUE))
} 
dice_b <- function(num_rolls) {
  return(sample(1:6, size = num_rolls, replace = TRUE, prob = c(1/9, 2/9, 1/9, 2/9, 1/9, 2/9)))
} 
dice_c <- function(num_rolls) {
  return(sample(1:6, size = num_rolls, replace = TRUE, prob = c(1/9, 1/9, 1/9, 2/9, 2/9, 2/9)))
}
choose_dice <- function(num_dice) {
  return(sample(c("A", "B", "C"), size = num_dice, replace = TRUE, prob = c(pa, pb, pc)))
}