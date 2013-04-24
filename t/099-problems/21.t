# P21 (*) Insert an element at a given position into a list.
#     Example:

#     moe> insert_at('new', 1, ['a', 'b', 'c', 'd'])
#     ['a', 'new', 'b', 'c', 'd']

use Test::More;

use Test::More;

sub insert_at($new, $at, @list) {
    my @r = @list[0 .. ($at-1)];
    @r.push($new, @list[$at .. (@list.length-1)]);
    @r.flatten
}

is_deeply(insert_at('new', 1, ['a', 'b', 'c', 'd']), ['a', 'new', 'b', 'c', 'd'], "... P21");

done_testing();

