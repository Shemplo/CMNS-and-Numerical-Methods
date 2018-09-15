# -*- coding: utf-8 -*-

import matplotlib.pyplot as plt
import numpy as np
import scipy.optimize as opt
from timeit import default_timer as timer

def func(k, x):
    return k * x * (1 - x)

def der(k, x):
    return abs(k * (1 - 2 * x))
    
def buildTree():
    eps = 0.0001
    print("Started building tree")
    start = timer()
    
    for k in np.arange(0.5, 4.0, 0.005):
        x0 = 1 - 1/k + 0.1
            
        xs = []
        rs = []
        
        finished = False
        
        while not finished:
            x0 = func(k, x0)
            xs.append(x0)
            rs.append(k)
            
            finished = abs(x0 - xs[len(xs) // 2]) < eps and len(xs) > 2
        
        plt.plot(rs[len(xs) // 2:], xs[len(xs) // 2:], 'k,')
        
    end = timer()
    print("Tree build in " + str(end - start))

    
def buildApproximation(r, maxIterations):
    dk = 0.01
    k = r

    #print("Started building approximation for r = " + str(r))
    start = timer()
    eps = 0.00001
    
    #s1 = opt.minimize_scalar(lambda x: der(k, x), bounds=[-eps, eps], method='bounded')
    #s2 = opt.minimize_scalar(lambda x: der(k, x), bounds=[1 - k - eps, 1 - k + eps], method='bounded')
    #q1 = der(k, s1.x)
    #eps1 = (1 - q1) / q1 * eps
    #q2 = der(k, s2.x)
    #eps2 = (1 - q2) / q2 * eps
    
    
    #print("q: " + str(q1) + " and " + str(q2)) 
    #if min(q1, q2) >= 1:
    #   print("Oooops! Problems.")
    #   return
    
    x0 = 1 - 1/r + 0.1
    
    count = 0
    
    t = np.arange(-1, 1, dk)
    s = func(k, t)
    plt.plot(t, s)
    
    s = t
    plt.plot(t, s)
    
    plt.plot([x0, x0], [x0, func(k, x0)], 'c')
    
    while True:
        x = func(k, x0)
        
        plt.plot([x, x], [func(k, x), x], 'c')
        plt.plot([x0, x], [func(k, x0), x], 'c--')
        
        if abs(x - x0) < eps or count > maxIterations:
            break

        
        x0 = x
        count+=1
    
    end = timer()
    print("Approximation found in " + str(end - start) + " in " + str(count) + " steps.")
    print("With epsilon " + str(eps))

    print("x = " + str(x))
    return x
r = float(input("Enter a R parameter: "))
max = int(input("Enter a number of maximum iterations: "))
#r = 2.5

fig = plt.figure()
fig.suptitle('ДЗ', fontsize=14, fontweight='bold')

ax = plt.subplot(121)
#fig.subplots_adjust(top=0.85)
ax.set_title('Бифуркационная диаграмма')

ax.set_xlabel('r')
ax.set_ylabel('x')
        
buildTree()

ax = plt.subplot(122)
        
result = buildApproximation(r, max)
ax.set_title('Последовательные приближения\n r = ' + str(r) + "\nПолучен результат x = " + str(result))
        
plt.show()
#plt.savefig('bif.png')