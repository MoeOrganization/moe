# P36 (**) Determine the prime factors of a given positive integer (2).
#       Construct a list containing the prime factors and their multiplicity.

#       moe> prime_factor_multiplicity(315)
#       [(3 => 2), (5 => 1), (7 => 1)]

#       Alternately, use a Map for the result.

#       moe> prime_factor_multiplicity(315)
#       {3 => 2, 5 => 1, 7 => 1}

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

# from P35
sub prime_factors($number) {
    my $factor = (2..(($number/2).Int)).first(($f) => { $number % $f == 0 && is_prime($f) });
    if ($factor.defined) {
        [$factor, prime_factors(($number/$factor).Int)].flatten
    } else {
        [$number.Int]
    }
}

sub prime_factor_multiplicity($number) {
    my %freq = {};
    prime_factors($number).each(($n) => { %freq{~$n} = +%freq{~$n} + 1; });
    %freq;
}

my %pfm_315 = prime_factor_multiplicity(315);

# .eqv method should work for hashes too
# ok(%pfm_315.eqv({3 => 2, 5 => 1, 7 => 1}),  "... P36");

is(%pfm_315{'3'}, 2, "... P36-1");
is(%pfm_315{'5'}, 1, "... P36-2");
is(%pfm_315{'7'}, 1, "... P36-3");

done_testing();
