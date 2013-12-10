attribute vec3 inPosition;
uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrix;
uniform float g_Time;

varying vec4 vertexPosition;
/*
* vertex shader template
*/
void main() { 

    vec4 position = vec4(inPosition,1.0);
    float o = sin(g_Time);
    float centerDistance = sqrt(pow(position.x, 2) + pow(position.y, 2));

    position.x = 0.5 + 0.5 * sin((position.y-centerDistance) * g_Time);
    position.y = inPosition.y;

    vertexPosition = position * g_WorldViewMatrix;
    gl_Position = g_WorldViewProjectionMatrix * position; 
    
}
