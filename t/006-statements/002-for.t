use Test::More;

{
    my $r = eval(
        "my $x = 0;" ~
        "my @x = 1 .. 10;" ~
        "for (my $i = 0; $i < @x.length; $i = $i + 1) {" ~
            "$x = $x + 1;" ~
        "}" ~
        "$x"
    );
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 10, '... got the expected value');
}

{
    my $r = eval(
        "my $a = 0;" ~
        "$a = $a + 1 for 1..5;" ~
        "$a"
    );
    ok(not($!.defined), '... the modifier statement with range worked correctly');
    is($r, 5, '... got the expected value');
}

{
    my $r = eval(
        "my $a = 0;" ~
        "$a = $a + $_ for [1,2,3];" ~
        "$a"
    );
    ok(not($!.defined), '... the modifier statement with list worked correctly');
    is($r, 6, '... got the expected value');
}

done_testing();
