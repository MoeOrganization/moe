# P33 (*) Determine whether two positive integer numbers are coprime.
#     Two numbers are coprime if their greatest common divisor equals 1.

#     moe> is_coprime_to(35, 64)
#     true

use Test::More;

sub gcd($a, $b) {
    $b == 0 ? $a : gcd($b, $a % $b)
}

sub is_coprime_to($a, $b) {
    gcd($a, $b) == 1
}

is(is_coprime_to(35, 64), true, "... P33");

done_testing();
