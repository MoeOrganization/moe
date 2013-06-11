use Test::More;

# qw operator

{
    my @result = qw{a b c};
    ok(@result.eqv(["a", "b", "c"]), "... qw operator works");
}
{
    my $x = "x";
    my @result = qw<$x y z>;
    ok(@result.eqv(['$x', 'y', 'z']), "... qw operator does not interpolate");
}

# qx operator

{
    is(qx{echo hello, world}, "hello, world\n", "... qx operator works");
    my $world = "world";
    is(qx{echo hello, $world}, "hello, world\n", "... qx operator interpolates");
}

done_testing();
