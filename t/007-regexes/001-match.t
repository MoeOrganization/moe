use Test::More;

ok("foo".match("foo"),  '... basic string match using .match method');
ok("foo".match(/^foo$/), '... basic regex match using .match method');

{
    my $str = "abrAcadAbbra";
    ok($str.match(/a.+A/), '... regex match on str variable using .match method');
}

{
    my $str = 'hello';
    ok($str.match(/h/), '... pattern match using regex using .match method');
    is($str, 'hello',   '..... with no side effect');
}

ok("foo" =~ /f/, "... string literal match with =~ operator");
ok("foo" =~ m/f/, "... string literal match with =~ and explicit 'm' operator");

{
    my $str = "foo";
    ok($str =~ /f/, "... string variable match with =~ operator");
    ok($str =~ m/f/, "... string variable match with =~ and explicit 'm' operator");
}

{
    my $str = "foo";
    ok($str =~ m{f}, "... string variable match with '{ }' as delimiters");
    ok($str =~ m[f], "... string variable match with '[ ]' as delimiters");
    ok($str =~ m(f), "... string variable match with '( )' as delimiters");
    ok($str =~ m<f>, "... string variable match with '< >' as delimiters");
}

done_testing();
