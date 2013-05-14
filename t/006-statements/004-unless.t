use Test::More;

{
    my $r = eval("unless (false) { 10 }");
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 10, '... got the expected value');
}

{
    my $r = eval("unless (true) { 10 } else { 20 }");
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 20, '... got the expected value');
}

{
    my $r = eval("unless (false) { 10 } elsif (true) { 20 }");
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 10, '... got the expected value');
}

{
    my $r = eval("unless (true) { 10 } elsif (true) { 20 }");
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 20, '... got the expected value');
}

{
    my $r = eval("unless (true) { 10 } elsif (false) { 20 } else { 30 }");
    ok(not($!.defined), '... the statement worked correctly');
    is($r, 30, '... got the expected value');
}

done_testing();
