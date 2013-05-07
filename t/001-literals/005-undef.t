use Test::More;

# test with raw literal
{
    ok(not(undef.defined), "... undef is not defined");
    ok(undef.isa("Undef"), "... undef isa Undef");
}

# test with variable
{
    my $x = undef;

    ok(not($x.defined), "... undef is not defined");
    ok($x.isa("Undef"), "... undef isa Undef");
}

# test with un-initialized variable
{
    my $x;

    ok(not($x.defined), "... undef is not defined");
    ok($x.isa("Undef"), "... undef isa Undef");
}

# test with object creation
{
    my $x = Undef.new;

    ok(not($x.defined), "... undef is not defined");
    ok($x.isa("Undef"), "... undef isa Undef");
}

done_testing();