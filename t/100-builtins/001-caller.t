use Test::More;

# caller returns the following:
# [ $package, $class, $sub_or_method_name, $invocant, @args ]


sub foo { [ caller() ] } 
sub bar { [ caller(), foo(), caller() ] } 
sub baz { [ caller(), bar(), caller() ] }
    
{
    is_deeply(
        baz(), 
        [
            [ ["main", undef, "baz", undef, []] ],
            [
                [ ["main", undef, "bar", undef, []], ["main", undef, "baz", undef, []] ],
                    [
                        [ ["main", undef, "foo", undef, []], ["main", undef, "bar", undef, []], ["main", undef, "baz", undef, []] ]
                    ],
                [ ["main", undef, "bar", undef, []], ["main", undef, "baz", undef, []] ]
            ],
            [ ["main", undef, "baz", undef, []] ]
        ],
        '... got the results of deep calls correctly'
    );
}

sub one   ($x) { caller($x) }
sub two   ($x) { one($x)    }
sub three ($x) { two($x)    }
sub four  ($x) { three($x)  }

{
    is_deeply(four(0), ["main", undef, "one",   undef, [0]], '... got the right stack trace info for level 0');
    is_deeply(four(1), ["main", undef, "two",   undef, [1]], '... got the right stack trace info for level 1');
    is_deeply(four(2), ["main", undef, "three", undef, [2]], '... got the right stack trace info for level 2');
    is_deeply(four(3), ["main", undef, "four",  undef, [3]], '... got the right stack trace info for level 3');
    is(four(4), undef,    '... got the right stack trace info for level 4');
}

package Foo { 
    sub bar { caller() } 
    sub baz { bar() } 
    package Bar {
        sub gorch { Foo::baz() } 
    }
}

{
    is_deeply(
        Foo::baz(),
        [["Foo", undef, "bar", undef, []], ["Foo", undef, "baz", undef, []]],
        '... tracking packages properly'
    );

    is_deeply(
        Foo::Bar::gorch(),
        [["Foo", undef, "bar", undef, []], ["Foo", undef, "baz", undef, []], ["Foo::Bar", undef, "gorch", undef, []]],
        '... tracking packages properly'
    );
}

class CallerTest {
    method one   { caller() }
    method two   { self.one }
    method three { self.two }
}

{
    my $t = CallerTest.new;
    is_deeply(
        $t.three,
        [["main", "CallerTest", "one", $t, []], ["main", "CallerTest", "two", $t, []], ["main", "CallerTest", "three", $t, []]],
        '... tracking classes and methods properly'
    );
}

package Foo {
    package Bar {
        class DeepCallerTest {
            method one   { caller() }
            method two   { self.one }
            method three { self.two }
        }
    }
}

{
    my $t = Foo::Bar::DeepCallerTest.new;
    is_deeply(
        $t.three,
        [["Foo::Bar", "DeepCallerTest", "one", $t, []], ["Foo::Bar", "DeepCallerTest", "two", $t, []], ["Foo::Bar", "DeepCallerTest", "three", $t, []]],
        '... tracking classes and methods properly'
    );
}

done_testing();

