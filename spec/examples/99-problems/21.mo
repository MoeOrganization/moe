# P21 (*) Insert an element at a given position into a list.
#     Example:

#     moe> insert_at('new', 1, ['a', 'b', 'c', 'd'])
#     ['a', 'new', 'b', 'c', 'd')

sub insert_at($new, $at, @list) {
    my @r = @list[0 .. ($at-1)];
    @r.push($new, @list[$at .. (@list.length-1)]);
    @r.flatten
}

say insert_at('new', 1, ['a', 'b', 'c', 'd'])