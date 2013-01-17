Git Guide
=========

What is this guide about?
-------------------------

The purpose of this guide is to help you understand how to use Git and GitHub
effectively when working on Moe, using the conventions we find most
comfortable.

We will introduce branches, pulls, pushes, rebasing and so on. One step at a
time, do not worry!

If you are already familiar with Git, feel free to move on to more advanced
topics.


Development flow
----------------

The flow of Moe is similar to GitHub flow. If you already know it, you'll
feel right at home!

Once you have a repository clone locally you can start contributing. The flow
is to create your own branch and work on it. Then once you're done, you want
to sync it with all changes done since you started working on it, and then
you submit your work, either in your own fork or the main fork. Let's go over
all the steps.

### cloning your repository

If you have a commit bit on the main repository, you can simply clone that:

    git clone git@github.com:MoeOrganization/moe.git

If you do not have a commit bit, you will need to fork the project (easily
done via the `Fork` button in the project's page. Then you can clone your
fork:

    git clone git@github.com:YourUsername/moe.git

    # and now to add the original path as the upstream
    git remote add upstream https://github.com/MoeOrganization/moe.git

You should now have a directory called ''moe'' with the entire project,
including all the commit history. You should read up on other Git commands
such as `git log` if you want to review the history.

### branching

In order to keep your contribution isolated from other code or documentation
changes, it's healthy to create a branch of development. That makes it easier
to review, approve, merge or even revise it, if necessary.

    git checkout -b mysuperawesomefeature master

This creates a new branch called ''mysuperawesomefeature'' (though you can use
dashes to make long names more readable), which is based on the ''master''
branch, which is the main project branch and where all works gets merged
eventually.

However, it doesn't just create the branch, it also places you on it, so you
can start working right away. All commits you will make from this point (until
you switch to a different branch) will be done in your new awesome branch.

### committing changes

Once you've created the files you wish to create and have edited whatever
content needs editing, you're ready to add and commit.

The act of `git add` will add new files that did not exist prior to your work,
while `git commit` introduces into the repo changes that were done to files
Git already knows (which are referred to as "tracked files" in Git-speak).

    vi Guide.mkdn # new file, it will need adding
    git add Guide.mkdn # now we can commit it
    git commit -m "created a new guide"

    cd src/main/scala/org/moe/
    vi Moe.scala # editing a tracked file
    git commit Moe.scala -m "cleaned up some Scala styling"

Note that `git add` and `git commit` are commands that have layers of
complexity and features that will not be covered and can be used in various
different ways that provide a more fluent work-flow coupled with advanced
features. Should you find the time, read more on these commands, especially
`git add -p`, which provides finer-grained control over content adding and
allows to review your work more easily before committing.

### rebasing in preparation for submission

So you finished all your work and it's sitting happily in your local commit
history. Are you sure it's ready for merge?

While you were working it's possible (and sometimes likely) that others have
done work as well. Perhaps some of that work was just merged to the main
branch ("master") of the project. This means that the project has had changes
introduced since you created your branch and - depending on how you look it -
either you're out of step with it, or it is out of step with you.

While this doesn't seem scary, it becomes quite the little annoyance if
you're depending on something that has changed: tests might break, code
might stop working, and merging itself might cause a conflict because two
people might have changed the same content.

What do we do? Rebase!

The act of rebasing is to get your code up to speed with changes that already
occured in the project after you created your branch. Rebasing places your
changes fresh on top of the existing ones.

    # first, we go to the main branch
    git checkout master

    # if you have a commit bit, you simply pull the last changes
    git pull origin master

    # if you work on your own GitHub fork, we pull from upstream
    git pull upstream master

    # now we can go back to our branch and rebase from master
    git checkout mysuperawesomefeature
    git rebase master

Now your code is sitting on top of all the changes that had happened since
you started working on it. Good job!

### submitting your contribution

It's always good manners to submit your contribution for approval instead of
pushing it directly, unless you've gained enough knowledge and experience that
it becomes unlikely you'll make changes that need reverting.

Since we have everything in a branch, we can simply push that branch remotely.

    git push origin mysuperawesomefeature

Now we need to alert the other developers that there's a new branch for review.
This is best done using the "Pull Request" button available on the Github
project page. Click it and follow the instructions.

