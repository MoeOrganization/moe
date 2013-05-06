# P31 (**) Determine whether a given integer number is prime.

#       moe> is_prime(7)
#       true

use Test::More;

# simplistic, not-very-efficient prime-checker
sub is_prime($number) {
    if ($number < 2) {
        false
    }
    else {
        !((2..($number.sqrt.Int)).first(-> ($f) { $number % $f == 0 }).defined)
    }
}

is(is_prime(7),  true,  "... P31-1");
is(is_prime(28), false, "... P31-2");

done_testing();
