use Test::More;

is("ABC".trans("ABC", "abc"), "abc", "... literal transliteration using Str.trans method");
is("ABC" =~ tr/ABC/abc/,      "abc", "... literal transliteration using tr operator");

{
    my $var = "ABC";
    is($var.trans("ABC", "abc"), "abc", "... variable transliteration using Str.trans method");
    is($var =~ tr/ABC/abc/,      "abc", "... variable transliteration using tr operator");
    is($var, "ABC", "... input variable is unchanged after transliteration");
}

is("ABC" =~ tr/A-C/a-c/,
   "abc",
   "... transliteration with character ranges");

is("ABC-DEF".trans("- AB-Z", "_ a-z"),
    "abc_def",
    "... If the first character is a dash it isn't part of a range");

is("ABC-DEF".trans("A-YZ-", "a-z_"),
    "abc_def",
    "... If the last character is a dash it isn't part of a range");

is("ABCDEF".trans( 'AB-E', 'ab-e' ),
    "abcdeF",
    "... The two sides can consists of both chars and ranges");

is("ABCDEFGH".trans( 'A-CE-G', 'a-ce-g' ),
    "abcDefgH",
    "... The two sides can consist of multiple ranges");

is("Whfg nabgure Zbr unpxre".trans('a-zA-Z', 'n-za-mN-ZA-M'),
    "Just another Moe hacker",
    "... Multiple ranges interpreted in string");

my $a = "abcdefghijklmnopqrstuvwxyz";
my $b = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

is($a.trans('a-z', 'A-Z'), $b);
is($b.trans('A-Z', 'a-z'), $a);
is($a.trans('b-y', 'B-Y'), 'aBCDEFGHIJKLMNOPQRSTUVWXYz');

is($b.trans('A-H-Z', 'a-h-z'), $a,
   '... ambiguous ranges combined');

is($b.trans('-H-Z', '_h-z'),
   'ABCDEFGhijklmnopqrstuvwxyz',
   '... leading ranges interpreted as string');

is($b.trans('A-H-', 'a-h_'), 'abcdefghIJKLMNOPQRSTUVWXYZ',
   '... trailing ranges interpreted as string');

is($b.trans('-A-H-', '_a-h_'), 'abcdefghIJKLMNOPQRSTUVWXYZ',
   '... leading, trailing ranges interpreted as string');

is("hello".trans("l", ""), "hello", "... empty replacement list uses search list");

# complement, squeeze/squash, delete

is('bookkeeper'.trans('a-z', 'a-z', 's'), 'bokeper',
    '... s flag (squash)');

is('bookkeeper'.trans('ok', '', 'd'), 'beeper',
    '... d flag (delete)');

is('ABC123DEF456GHI'.trans('A-Z', 'x'), 'xxx123xxx456xxx',
    '... no flags');

is('ABC123DEF456GHI'.trans('A-Z', 'x', 'c'),'ABCxxxDEFxxxGHI',
    '... with c (complement) flag');

is('ABC111DEF222GHI'.trans('0-9', 'x', 's'),'ABCxDEFxGHI',
    '... with s (squash) flag');

is('ABC111DEF222GHI'.trans('A-Z', 'x', 'cs'),'ABCxDEFxGHI',
    '... with s and c');

is('ABC111DEF222GHI'.trans('A-Z', '', 'cd'),'ABCDEFGHI',
    '... with d and c');

is('Good&Plenty'.trans('len', 'x'), 'Good&Pxxxty',
    '... no flags');

is('Good&Plenty'.trans('len', 'x', 's'), 'Good&Pxty',
    '... squashing depends on replacement repeat, not searchlist repeat');

is('Good&Plenty'.trans('len', 't', 's'), 'Good&Ptty',
    '... squashing depends on replacement repeat, not searchlist repeat');

done_testing();
