# P35 (**) Determine the prime factors of a given positive integer.
#       Construct a flat list containing the prime factors in ascending order.

#       moe> prime_factors(315)
#       [3, 3, 5, 7]

use Test::More;

# from P31
# simplistic, not-very-efficient prime-checker
sub is_prime($number) {
    if ($number < 2) {
        false
    } else {
        !((2..($number.sqrt.Int)).first(($f) => { $number % $f == 0 }).defined)
    }
}

sub prime_factors($number) {
    my $factor = (2..(($number/2).Int)).first(($f) => { $number % $f == 0 && is_prime($f) });
    if ($factor.defined) {
        [$factor, prime_factors(($number/$factor).Int)].flatten
    }
    else {
        [$number.Int]
    }
}

ok(prime_factors(315).eqv([3, 3, 5, 7]),  "... P35");

done_testing();
