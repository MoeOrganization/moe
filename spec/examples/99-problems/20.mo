# P20 (*) Remove the Kth element from a list.
#     Return the list and the removed element in a Tuple. Elements are numbered from 0.

#     Example:

#     moe> remove_at(1, ['a', 'b', 'c', 'd'])
#     [['a', 'c', 'd'],'b']

sub remove_at($i, @list) {
    my $removed = @list[$i];
    my @rest = @list[0 .. ($i-1)];
    @rest.push(@list[($i+1) .. (@list.length-1)]);
    [@rest.flatten, $removed]
}

say remove_at(1, ['a', 'b', 'c', 'd']);