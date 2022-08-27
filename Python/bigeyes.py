#
#   Big Eyes Test Code 
#

import pygame
import math


SCREEN_WIDTH    =   800
SCREEN_HEIGHT   =   600


def update():
    pass

def draw():
    screen.fill((0, 0, 0))

    def draw_eye(eye_x, eye_y):
        mouse_x, mouse_y = pygame.mouse.get_pos()

        distance_x = mouse_x - eye_x
        distance_y = mouse_y - eye_y
        distance = min(math.sqrt(distance_x**2 + distance_y**2), 100)
        angle = math.atan2(distance_y, distance_x)

        pupil_x = eye_x + (math.cos(angle) * distance)
        pupil_y = eye_y + (math.sin(angle) * distance)s

        pygame.draw.circle(screen, (255, 255, 255), [eye_x, eye_y], 160 )
        pygame.draw.circle(screen, (0, 0, 100), [pupil_x, pupil_y], 50 )


        pygame.display.update()


    draw_eye(200, 300)
    draw_eye(600, 300)



pygame.init()
pygame.display.set_caption("Pro Makers - Big Eyes")
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))


clock = pygame.time.Clock()
while True:
    clock.tick(10)
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            sys.exit()

        
    draw()



