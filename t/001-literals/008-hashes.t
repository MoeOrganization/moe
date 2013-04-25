use Test::More;

{
    my %x = { one => 1, two => 2, three => 3 };

    ok(%x.isa('Hash'), '... this isa Hash');

    is(%x{'one'},   1, '... got the expected value for key "one"');
    is(%x{'two'},   2, '... got the expected value for key "two"');
    is(%x{'three'}, 3, '... got the expected value for key "three"');

    is(%x.at_key('one'),   1, '... got the expected value for &at_key "one"');
    is(%x.at_key('two'),   2, '... got the expected value for &at_key "two"');
    is(%x.at_key('three'), 3, '... got the expected value for &at_key "three"');

    is(%x.exists('one'),  true, '... &exists worked');

    is(%x.exists('four'), false, '... &exists worked');
    is(%x{'four'}, undef, '... got the expected value for key "four"');
    is(%x.at_key('four'), undef, '... got the expected value for &at_key "four"');

    is(%x.bind_key('four', 4), 4, '... &bind_key works');
    
    is(%x.exists('four'), true, '... &exists worked');
    is(%x{'four'}, 4, '... got the expected value for key "four"');
    is(%x.at_key('four'), 4, '... got the expected value for &at_key "four"');

    is_deeply(%x.slice('one', 'four'), [1, 4], '... got the expected values from &slice');

    # NOTE: 
    # the next 4 tests rely on hash ordering consistency
    # obviously this is not what we want, so just keep 
    # that in mind as we continue this.
    # - SL

    is_deeply(%x.keys, ['one', 'three', 'four', 'two'], '... got the expected values from &keys');
    is_deeply(%x.values, [1, 3, 4, 2], '... got the expected values from &values');

    is_deeply(%x.kv, [['one', 1], ['three', 3], ['four', 4], ['two', 2]], '... got the expected values from &kv');
    is_deeply(%x.pairs, ['one' => 1, 'three' => 3, 'four' => 4, 'two' => 2], '... got the expected values from &pairs');

    is(%x.clear, undef, '... got the expected value from &clear');
    is_deeply(%x.kv, [], '... got the expected values from &kv (after &clear)');
}

done_testing();
