/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */
'use strict';

import React, {Component} from 'react';
import {Platform, StyleSheet, FlatList, Text, TouchableOpacity, TextInput, Dimensions, View} from 'react-native';

const numColumns = 10;
export default class App extends Component {
  constructor(props){
    super(props);
    this.state = {text: "", data: [], toGuess: Math.floor((Math.random() * 100) + 1)}

    for (var i = 0; i < 100; i++){
      this.state.data.push({key: i+1, color: "#8499b1"})
    }
  }

  color = (beginning, end, color) => {
    this.setState(state => {
      const data = state.data.map((item, j) => {
        if (j >= beginning && j <= end) {
          return {key: j+1, color: color};
        } else {
          return item;
        }
      });
      return {
        data,
      };
    });
  }

  onPressGuess = () => {
    var userGuess = parseInt(this.state.text);
    if (userGuess == this.state.toGuess){
      this.color(0, 99, "#939196")
      this.color(userGuess-1, userGuess-1, "#7a8450")
    } else {
      if (userGuess < this.state.toGuess){
        this.color(0, userGuess-1, "#939196")
      } else {
        this.color(userGuess-1, 99, "#939196")
      }
    }
    this.setState({text: ""});
  }

  onPressReset = () => {
    this.color(0, 99, "#8499b1")
    this.setState({text: "", toGuess: Math.floor((Math.random() * 100) + 1)});
  }

  renderItem = ({item, index}) => {
    return(
      <View style={{
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: item.color,
        color: 'black',
        flex: 1,
        margin: 1,
        height: Dimensions.get('window').width/numColumns
      }}>
        <Text>{item.key}</Text>
      </View>
    )
  }

  render() {
    return (
      <View style={{backgroundColor: "black", height: '100%'}}>
        <FlatList
          data={this.state.data}
          renderItem={this.renderItem}
          numColumns={numColumns}
        />
        <View style={styles.rowContainer}>
          <TextInput
            style={styles.text}
            onChangeText={(text) => this.setState({text})}
            value={this.state.text}
            keyboardType='numeric'
          />
          <TouchableOpacity
            style={styles.buttonGuess}
            onPress={this.onPressGuess}>
            <Text> Guess </Text>
          </TouchableOpacity>
        </View>
        <TouchableOpacity
            style={styles.buttonReset}
            onPress={this.onPressReset}>
            <Text> Reset </Text>
          </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  rowContainer: {
    margin: 1,
    height: 40,
    flexDirection: 'row'
  },
  text: {
    height: 40,
    margin: 2, 
    color: "white",
    borderColor: 'gray', 
    borderWidth: 1,
    width: "65%"
  },
  buttonGuess: {
    height: 40,
    margin: 2, 
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: "#8499b1",
    color: "white",
    width: "33%"
  },
  buttonReset: {
    height: 40,
    margin: 3, 
    marginTop: 4,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: "#8499b1",
    color: "white",
  }
});
