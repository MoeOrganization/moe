# P32 (**) Determine the greatest common divisor of two positive integer numbers.
#         Use Euclid's algorithm.

#     moe> gcd(36, 63)
#     9

use Test::More;

sub gcd($a, $b) {
    $b == 0 ? $a : gcd($b, $a % $b)
}

is(gcd(36, 63), 9, "... P32");

done_testing();
