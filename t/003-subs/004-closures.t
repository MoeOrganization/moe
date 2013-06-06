use Test::More;

sub mk_adder ($x) { ($y) => { $x + $y } }

{
    my $adder_1 = mk_adder(2);
    is($adder_1.(2), 4, '... got the value we expected from the first');

    my $adder_2 = mk_adder(10);
    is($adder_2.(2), 12, '... got the value we expected from the second');
    is($adder_1.(2), 4, '... still got the value we expected from the first');
}

sub mk_modifier ($x) { ($y?) => { $x = $y if $y; $x } }

{
    my $mod_1 = mk_modifier(10);
    is($mod_1.(20), 20, '... the value has been modified');
    is($mod_1.(), 20, '... the value still has been modified');

    my $mod_2 = mk_modifier(100);
    is($mod_2.(), 100, '... the value has not yet been modified');    
    is($mod_2.(50), 50, '... the value has been modified');

    is($mod_1.(), 20, '... the first one has not been affected');    
}

{
    my $_x = 10;
    my $_y = 20;

    sub closed_over_1 ($x?, $y?) {
        $_x = $x if $x.defined;
        $_y = $y if $y.defined;
        [ $_x, $_y ]
    }

    sub closed_over_2 ($x?, $y?) {
        $_x = $x if $x.defined;
        $_y = $y if $y.defined;
        [ $_x, $_y ]
    }

    is_deeply(closed_over_1(), [ 10, 20 ], '... got the right values back');

    is($_x, 10, '... x is still 10');
    is($_y, 20, '... y is still 20');

    is_deeply(closed_over_2(), [ 10, 20 ], '... got the right values back');

    is($_x, 10, '... x is still 10');
    is($_y, 20, '... y is still 20');    

    is_deeply(closed_over_1(30), [ 30, 20 ], '... got the right values back');
    is_deeply(closed_over_2(), [ 30, 20 ], '... got the right values back');

    is($_x, 30, '... x is now 30');
    is($_y, 20, '... y is still 20');    

    is_deeply(closed_over_1(), [ 30, 20 ], '... got the right values back');
    is_deeply(closed_over_2(1, 2), [ 1, 2 ], '... got the right values back');
    is_deeply(closed_over_1(), [ 1, 2 ], '... got the right values back');

    is($_x, 1, '... x is now 1');
    is($_y, 2, '... y is now 2');
}

done_testing();