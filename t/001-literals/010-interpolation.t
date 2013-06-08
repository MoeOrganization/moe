use Test::More;

{
    # interpolating into double quotes results in a Str
    my $a = 3;
    ok("$a".isa("Str"), '... "$a" results in a Str');
    ok("{3}".isa("Str"), '... "{3}" results in a Str');
}

{
    my $w = 'work';
    is("this should $w", 'this should work', '... scalar interpolates');
}

{
    my @array = 1..3;
    is("@array[]", "[1, 2, 3]", "... @array[] interpolates");
    is("@array", "@array", '... @array (without brackets) doesnt interpolate');
    is("@array[1]", "2", '... @array element interpolates');
}

{
    my %hash = {a => 1, b => 2};
    ok("%hash{}" ne '%hash{}', '... %hash{} interpolates');
    is("%hash", "%hash", '... %hash (without brackets) doesnt interpolate');
    is("%hash{'b'}", "2", '... %hash element interpolates');
}

{
    sub foo { 42 }
    is("&foo()", "42", '... &foo() interpolates');
    is("&foo", "&foo", '... &foo (without brackets) doesnt interpolate');
}

{
    class A {
        has $!foo = 42;
        method foo { $!foo }
    }

    my $a = A.new;
    is("$a.foo()", "42", '... method call $a.foo() interpolates');
    is("$a.foo", '$a.foo', '... method call $a.foo (without brackets) doesnt interpolate');
}

done_testing();
