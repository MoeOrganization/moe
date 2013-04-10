package Tree {

    class NAry {
        has $!node;
        has $!parent;

        has @!children = [];

        BUILD ($node, @children?) { 
            $!node = $node;
            if (@children) {
                @!children = @children;
                self.fix_parent;
            }
        }

        method node { $!node }

        method parent ($parent?) {
            if ($parent) { $!parent = $parent; }
            $!parent
        }

        method depth { $!parent ? $!parent.depth + 1 : 0 }

        method traverse (&f) {
            &f.(self);
            @!children.each(-> ($c) { 
                $c.traverse(&f) 
            })
        }

        submethod fix_parent { 
            @!children.each(-> ($c) { $c.parent(self) }) 
        }
    }

}