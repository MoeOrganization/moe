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
    is("@array[]", "[1, 2, 3]", '... @array[] interpolates');
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

{
    my $a = 2; my $b = 21;
    is("The Answer = { $a * $b }", "The Answer = 42", '... code block interpolates');
    is("The Answer = \{ { $a * $b } \}", 'The Answer = { 42 }', '... escaped braces dont make code block');
}

# tests adapted from https://github.com/perl6/roast/blob/master/S02-literals/misc-interpolation.t

my $world = "World";
my $number = 1;
my @list  = [1, 2];
my %hash  = {1 => 2};
sub func { "func-y town" }
sub func_w_args($x,$y) { "[$x][$y]" }

# Double quotes
is("Hello $world", 'Hello World', '... double quoted string interpolation works');
is("@list[] 3 4", '[1, 2] 3 4', '... double quoted list interpolation works');
is("@list 3 4", '@list 3 4', '... array without empty square brackets does not interpolate');
is("%hash{}", '{1 => 2}', '... hash interpolation works');
is("%hash", '%hash', '... hash interpolation does not work if not followed by {}');
is("Wont you take me to &func()", 'Wont you take me to func-y town', '... closure interpolation');
is("2 + 2 = { 2+2 }", '2 + 2 = 4', '... double quoted closure interpolation works');

is("&func() is where I live", 'func-y town is where I live', "... make sure function interpolation doesn't eat all trailing whitespace");
is("$number {$number}", '1 1', '... number inside and outside closure works');
is("$number {my $number=2}", '1 2', '... local version of number in closure works');
is("$number {my $number=2} $number", '1 2 1', '... original number still available after local version in closure: works' );

is("&func. () is where I live", '&func. () is where I live', '... "&func. ()" should not interpolate');

# Single quotes
is('Hello $world', 'Hello $world', '... single quoted string interpolation does not work (which is correct)');
is('2 + 2 = { 2+2 }', '2 + 2 = { 2+2 }', '... single quoted closure interpolation does not work (which is correct)');
is('$world @list[] %hash{} &func()', '$world @list[] %hash{} &func()', '... single quoted string interpolation does not work (which is correct)');

# FIXME: this test fails because in Perl6, closures don't interpolated
# when braces are used as delimiters. Moe currently doesn't make this
# distinction
# is(qq{a{chr 98}c}, 'a{chr 98}c', "curly brace delimiters interfere with closure interpolation");

done_testing();
