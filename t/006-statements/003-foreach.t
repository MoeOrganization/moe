use Test::More;

{
    my $r = eval(
        "my $x = 0;" ~
        "my @x = 1 .. 10;" ~
        "foreach my $i (@x) {" ~
            "$x = $x + 1;" ~
        "}" ~
        "$x"
    );
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 10, '... got the expected value');
}

{
    my $r = eval(
        "my $x = 0;" ~
        "foreach my $i (1 .. 10) {" ~
            "$x = $x + $i;" ~
        "}" ~
        "$x"
    );
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 55, '... got the expected value');
}

done_testing();