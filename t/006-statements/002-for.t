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

done_testing();