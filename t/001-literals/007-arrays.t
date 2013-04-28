use Test::More;

{
    my @x = [ 1, 2, 3 ];

    ok(@x.isa('Array'), '... this isa Array');

    is(@x[0], 1, '... found the correct item at index 0');
    is(@x[1], 2, '... found the correct item at index 1');
    is(@x[2], 3, '... found the correct item at index 2');

    is(@x.at_pos(0), 1, '... found the correct item at index 0');
    is(@x.at_pos(1), 2, '... found the correct item at index 1');
    is(@x.at_pos(2), 3, '... found the correct item at index 2');

    is(@x.bind_pos(1, 5), 5, '... set the item at index 1');
    is(@x[1], 5, '... found the correct item at index 1');
    is(@x.at_pos(1), 5, '... found the correct item at index 1');

    is(@x.length, 3, '... the length is 3');

    is(@x.bind_pos(3, 1), 1, '... set the item at index 3 (out of bounds)');    

    is(@x[3], 1, '... found the correct item at index 1');
    is(@x.at_pos(3), 1, '... found the correct item at index 1');

    is(@x.length, 4, '... the length is 4 now');

    is(@x[5], undef, '... found the correct lack of an item at index 5');
    is(@x.at_pos(5), undef, '... found the correct lack of an item at index 5');

    is(@x.length, 4, '... the length is still 4');

    is(@x.head, 1, '... got the expected value from &head');
    is_deeply(@x.tail, [ 5, 3, 1 ], '... got the expected value from &tail');

    is(@x.shift, 1, '... got the expected value from &shift');
    is(@x.length, 3, '... the length is now 3');    
    is_deeply(@x, [ 5, 3, 1 ], '... the contents of the array have changed');

    is(@x.pop, 1, '... got the expected value from &pop');
    is(@x.length, 2, '... the length is now 2');    
    is_deeply(@x, [ 5, 3 ], '... the contents of the array have changed');

    is(@x.unshift(10), 3, '... got the expected value from &unshift(10)');
    is(@x.length, 3, '... the length is now 3');    
    is_deeply(@x, [ 10, 5, 3 ], '... the contents of the array have changed');

    is(@x.push(1), 4, '... got the expected value from &push(1)');
    is(@x.length, 4, '... the length is now 4');    
    is_deeply(@x, [ 10, 5, 3, 1 ], '... the contents of the array have changed');    

    is_deeply(@x.slice(0, 2), [ 10, 3 ], '... got the expected value from &slice');        

    is_deeply(@x.range(1, 2), [ 5, 3 ], '... got the expected value from &range');  
    is_deeply(@x.range(1, @x.length), [ 5, 3, 1 ], '... got the expected value from &range');    

    is_deeply(@x.reverse, [ 1, 3, 5, 10 ], '... got the expected value from &reverse');

    is(@x.join, "10531", '... got the expected value from &join');
    is(@x.join(", "), "10, 5, 3, 1", '... got the expected value from &join($sep)');

    is_deeply(@x.map(-> ($x) { $x + 10 }), [ 20, 15, 13, 11 ], '... got the expected value from &map');

    is_deeply(@x.grep(-> ($x) { ($x % 2) == 0 }), [ 10 ], '... got the expected value from &grep');

    is(@x.reduce(-> ($a, $b) { $a + $b }), 19, '... got the exepected value from &reduce');
    is(@x.reduce(-> ($a, $b) { $a + $b }, 100), 119, '... got the exepected value from &reduce($init)');    

    is(@x.first(-> ($x) { $x > 5 }), 10, '... got the exepected value from &first');

    is(@x.max, 10, '... got the exepected value from &max');
    is(@x.min, 1,  '... got the exepected value from &min');

    is(@x.sum, 19, '... got the exepected value from &sum');

    ok(@x.eqv([10, 5, 3, 1 ]), '... got the expected value from &eqv');

    is(@x.exists(10), true,  '... got the expected value from &exists');    
    is(@x.exists(11), false, '... got the expected value from &exists');    

    is_deeply(@x.zip(['a', 'b', 'c', 'd']), [[10, 'a'], [5, 'b'], [3, 'c'], [1, 'd']], '... got the expected value from &zip');

    is_deeply(@x.kv, [[0, 10], [1, 5], [2, 3], [3, 1]], '... got the expected value from &kv');

    is(@x.clear, undef, '... got the expected value from &clear');
    is(@x.length, 0, '... the length is now 0');    
}

{ 
    my @x = [ 'foo', 'bar', 'baz' ];
    is(@x.each(-> ($x) { $x.chop }), undef, '... got the exepected value from &each');
    is_deeply(@x, [ 'fo', 'ba', 'ba' ], '... the array has been changed');
}

{ 
    my @x = [ 'foo', 'bar', 'baz' ];
    is(@x.maxstr, 'foo', '... got the exepected value from &maxstr');
    is(@x.minstr, 'bar', '... got the exepected value from &minstr');
}

{ 
    my @x = [ [1, 'foo'], [2, 'bar'], [3, 'baz'] ];
    is_deeply(@x.flatten, [ 1, 'foo', 2, 'bar', 3, 'baz' ], '... got the expected value from &flatten');
}

{ 
    my @x = [1, 2] x 3;
    is_deeply(@x.flatten, [ 1, 2, 1, 2, 1, 2 ], '... got the expected value from &infix:<x>');
    is_deeply(@x.uniq, [1, 2], '... got the expected value from &uniq');
}

# TODO:
# - shuffle
# - classify
# - categorize

done_testing();
