**概述**

机器人拥有自己的坐标，机器人的移动速度相同，1unit/s

设计一个异步机器人算法，每个机器人都有自己的一个坐标点，

算法开始机器人会随机分布在坐标系内，拥有自己的随机坐标

机器人可以处于激活状态和睡眠状态

处于激活状态的机器人拥有三个阶段，观察（look），计算（compute)，移动（move）三种状态，机器人是异步行动的，每个机器人都可能处于计算，观察，或者移动三种状态之一

机器人分为两组，一组聚集（gathering）一组为分散（Circle）

聚集组会在中心点（原点）集合，最终所有聚集组的机器人都在原点（坐标都是0，0）.

分散组会形成一个圆圈，最终所有机器人的坐标点都会在这个圆圈上。

有方法记录圆圈，有方法每秒钟记录一次机器人坐标，最后坐标和圆圈每秒钟输出成图

**圆圈的形成**

1，以距离原点最远的分散组(circle)的机器人（永远不动）为半径形成一个圆圈，此圆圈为分散组的终点，在此圆上机器人永远处于睡眠状态除了聚集组的机器人。

2，每个机器人都形成一个圆，都在自己的圆圈上。所有机器人的圆都是同心圆，圆点为原点（0，0）；

3，只要圆圈上有机器人，圆圈就会在，反之，如果圆圈上一个机器人都没有，圆圈就会消除

**聚集在中心点**

聚集组的机器人（到中心点时自动睡眠）在原点上永远不被激活

**机器人的活动**

观察function要记录所有机器人的坐标（一瞬间）

计算compute（一瞬间）要避免机器人相撞；（算法所在）

Reduce Phase:
1. 执行条件（dense configuration）： 没有multiplicity points 并且最外层有超过四个robots
2. move的robots一定是在non-pivotal location
3. 需要移动的robots不管是哪一组，都是往里一圈走，并且距离为两圈直线距离的一半
4. 如果robot在最内圈则向原点方向走，距离为一半 

移动function要有角度的速度参数（是有时间的，因为有速度），在创建机器人时速度参数就固定了，角度参数是可变的，聚集（Gathering）机器人默认朝向圆心移动，分散（circle）机器人默认朝向圆心外移动

机器人的活动要做成可视化的（可用Jpanel或其他）。

**机器人的激活状态**
机器人运行特定的时间自动变为睡眠状态 例如 5s， 然后其他机器人启动，执行look compute move， 从睡眠状态重新被激活的机器人不继续执行睡眠前的操作， 而是重新观察 计算 移动



要求，程序启动时，两组机器人自动移动到圆心和圆外，程序可以在中间暂停。



***Summarize****

The robot has its own coordinates, and the robot moves at the same speed: 1unit/s

Designing an asynchronous robot algorithm where each robot has its own coordinate point,

The algorithm starts with the robot randomly distributed in the coordinate system, having its own random coordinates

The robot can be active and asleep

The robot in the active state has three phases, look (look), compute (compute), move (move) three states, the robot is asynchronous action, each robot may be in the calculation, observation, or move one of the three states

The robots are divided into two groups, a gathering group and a Circle group.

The gathering group will be assembled at the center point (origin), and eventually all the robots in the gathering group will be at the origin (coordinate 0,0).

The circle group form a circle, and eventually the coordinates of all the robots will be on this circle.

There is a method to record the circle, and a method to record the robot coordinates every second, and finally the coordinates and the circle are output as a graph every second

***Circle formation***

1. Form a circle with the radius of a gathering group robot (never moving) farthest from the origin of the circle, which is the end point of the circle, and the robot is always asleep on this circle except for the robot in the gathering group.

2, each robot forms a circle and on its own circle. The circles of all robots are concentric, and the point is the origin (0,0);

3. As long as there is a robot, the circle will be on the robot; Conversely, if there is no robot on the circle, the circle will be eliminated

***Gather at a central point**

Robots that assemble groups (sleep automatically when they reach the center point) are never activated at the origin

**Robot Activity：**

Observe function to record the coordinates of all robots (instant)

compute (for a moment) to avoid robot collision; (Where the algorithm is)

Reduce Phase:

dense configuration: there are no multiplicity points and more than 4 robots in the outermost layer；
The move robots must be in a non-pivotal location；
The robots that need to move, regardless of the group, walk in one circle, and the distance is half the linear distance of two circles
If the robot is in the innermost circle, it goes in the direction of the origin, half the distance
The speed parameter of the moving function should have an Angle (there is time, because there is speed), and the speed parameter is fixed when the robot is created, and the Angle parameter is variable. The Gathering robot moves toward the center of the circle by default, and the dispersion robot moves outside the center of the circle by default

The robot's activity should be visualized (Jpanel or otherwise).

**Robot activation state**
The robot runs for a specific period of time and automatically changes to the sleep state such as 5s, then other robots start and perform the look compute move. The robot that is reactivated from the sleep state does not continue to perform the pre-sleep operation, but re-observes the computational move



When the program is started, the two groups of robots automatically move to the center and outside the circle, and the program can be paused in the middle.











