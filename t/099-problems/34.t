# P34 (**) Calculate Euler's totient function phi(m).

#     Euler's so-called totient function phi(m) is defined as the
#     number of positive integers r (1 <= r <= m) that are coprime to
#     m.

#     moe> totient(10)
#     4

use Test::More;

sub gcd($a, $b) {
    $b == 0 ? $a : gcd($b, $a % $b)
}

sub is_coprime_to($a, $b) {
    gcd($a, $b) == 1
}

sub totient($n) {
    (1..($n-1)).grep(($m) => { is_coprime_to($m, $n) }).length
}

is(totient(10), 4, "... P34");

done_testing();
