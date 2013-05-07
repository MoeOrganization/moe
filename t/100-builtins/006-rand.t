use Test::More;

{
    my $x = rand();
    ok($x.isa("Num"), '... rand gave us back a number');
    ok($x < 1, '... rand is less than 1');
    ok($x > 0, '... rand is greater than 0');
}

{
    my $x = rand(1);
    ok($x.isa("Num"), '... rand w/ seed gave us back a number');
    ok($x < 1, '... rand is less than 1');
    ok($x > 0, '... rand is greater than 0');
}

done_testing();