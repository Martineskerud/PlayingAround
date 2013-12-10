attribute vec3 inPosition;
attribute vec4 inTexCoord;
uniform mat4 WorldViewProjectionMatrix;

varying vertexPosition;
varying vec4 texCoord;

void main() { 

vec4 position = vec4(inPosition, 1.0);
texCoord= inTexCoord;

    gl_Position = g_WorldViewProjectionMatrix * position;
}
