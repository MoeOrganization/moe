use Test::More;

is("foo".subst(/^f/, "F"), "Foo", '... basic regex substitution');
is("foo".subst(/o/, "0"), "f0o", '... basic regex substitution');
is("foo".subst(/o/, "0", "g"), "f00", '... basic regex substitution with global flag');

{
    my $str = "abrAcadAbbra";
    is($str.subst(/a.+A/, ""), "bbra", '... regex substitution on str variable');
    is($str.subst(/a.+?A/, ""), "cadAbbra", '... regex substitution on str variable');
    is($str, "abrAcadAbbra", '... with no side effect');
}

is("foo" =~ s/f/g/, "goo", "... string literal substitution with =~ operator");
is("foo" =~ s/o/0/g, "f00", "... string literal global substitution with =~ operator");

{
    my $str = "foo";
    is($str =~ s/f/g/, "goo", "... str variable substitution with =~ operator");
    is($str =~ s/o/0/g, "f00", "... str variable global substitution with =~ operator");
    is($str, "foo", "... with no side effect")
}

{
    my $str = "foo";
    is($str =~ s/(.)/$1$1/, "ffoo", "... str variable substitution with capture");
    is($str =~ s/(.)/$1$1/g, "ffoooo", "... str variable global substitution with capture");
    is($str =~ s/(.)(.)/$2$1/, "ofo", "... str variable substitution with capture");
}

{
    my $str = "foo";
    is($str =~ s{(.)}{$1$1}, "ffoo", "... str variable substitution with '{ }' as delimiters");
    is($str =~ s[(.)][$1$1], "ffoo", "... str variable substitution with '[ ]' as delimiters");
    is($str =~ s((.))($1$1), "ffoo", "... str variable substitution with '( )' as delimiters");
    is($str =~ s<(.)><$1$1>, "ffoo", "... str variable substitution with '< >' as delimiters");
    is($str =~ s[(.)]{$1$1}, "ffoo", "... str variable substitution with different delimiter pairs");
}

done_testing();
