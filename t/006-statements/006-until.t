use Test::More;

{
    my $r = eval(
                 'my $x = 0;' ~
                 'until ($x > 10) {' ~
                 '    $x = $x + 1;' ~
                 '}' ~
                 '$x'
                );
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 11, '... got the expected value');
}

{
    my $r = eval(
                 'my $a = 0;' ~
                 '$a = $a + 1 until $a > 5;' ~
                 '$a'
                );
    ok(not($!.defined), '... the modifier statement worked correctly');
    is($r, 6, '... got the expected value');
}

done_testing();
