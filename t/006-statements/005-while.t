use Test::More;

{
    my $r = eval(
                 "my $x = 0;" ~
                 "while ($x < 10) {" ~
                 "    $x = $x + 1;" ~
                 "}" ~
                 "$x"
                );
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 10, '... got the expected value');
}

{
    my $r = eval(
                 "my $a = 0;" ~
                 "$a = $a + 1 while $a < 5;" ~
                 "$a"
                );
    ok(not($!.defined), '... the modifier statement worked correctly');
    is($r, 5, '... got the expected value');
}

done_testing();
